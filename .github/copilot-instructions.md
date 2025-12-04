# BoxLang Web Support Module - AI Agent Instructions

## Project Overview

This is a **BoxLang module** that provides web request/response mocking capabilities for the BoxLang CLI runtime. It enables testing web applications without a web server by simulating HTTP exchanges.

**⚠️ CRITICAL**: This module is for testing only. Do not install it into a web server as it will cause jar conflicts.

## Architecture

### Hybrid Language Design
- **Java layer** (`src/main/java/`): Core implementation of `MockHTTPExchange` class that implements `IBoxHTTPExchange` interface
- **BoxLang layer** (`src/main/bx/`): Module configuration and Built-In Functions (BIFs) that expose functionality
- **Service Loader pattern**: Uses Gradle ServiceLoader plugin to register BIFs at compile time

### Key Components
- `MockHTTPExchange.java`: Mock implementation of HTTP exchange with request/response tracking
- `ModuleConfig.bx`: Module descriptor defining settings and lifecycle hooks
- `GetMockServer.bx`: BIF to retrieve/create mock server instances (stores in request scope)
- `StartMockRequest.bx`: BIF to execute simulated web requests

## BoxLang Module Conventions

### Module Structure
```
src/main/bx/
  ModuleConfig.bx          # Module entry point
  bifs/*.bx                # Custom Built-In Functions
  tags/*.bx                # Custom tags (if needed)
  interceptors/*.bx        # Interceptors (if needed)
```

### Module Lifecycle
1. **ModuleConfig.bx** is the entry point - BoxLang runtime auto-injects these properties:
   - `boxRuntime`: BoxLang runtime instance
   - `log`: Module-specific logger
   - `moduleRecord`: Metadata about this module
2. `configure()` method: Define `settings`, `interceptors`, `customInterceptionPoints`
3. Module creates internal mapping: `bxModules.{this.mapping}` → `bxModules.bxWebSupport`

### BIF Development Pattern
All BIFs follow this pattern:
```boxlang
import java:fully.qualified.ClassName  // Import Java classes

@BoxBIF
class {
    property name="boxRuntime";       // Auto-injected
    property name="log";              // Auto-injected
    property name="moduleRecord";     // Auto-injected

    function invoke( /* parameters */ ) {
        // Access module settings via moduleRecord.settings
        // Return value becomes BIF result
    }
}
```

## Build System (Gradle)

### Essential Commands
```bash
./gradlew downloadBoxLang        # Download BoxLang dependency (required before first build)
./gradlew shadowJar              # Compile Java + BoxLang, create fat JAR
./gradlew test                   # Run JUnit tests
./gradlew build                  # Full build: compile, test, create module structure
./gradlew spotlessApply          # Format Java code (required before commits)
./gradlew spotlessCheck          # Verify code formatting (runs in CI)
```

### Build Process Flow
1. `compileJava` → Java sources compiled
2. `serviceLoaderBuild` → Generates `META-INF/services/` files for BIF registration
3. `shadowJar` → Creates fat JAR with dependencies in `build/libs/`
4. `createModuleStructure` → Copies JAR + `.bx` files → `build/module/`
   - Token replacement: `@build.version@`, `@build.number@` in all `.bx` and metadata files
5. `zipModuleStructure` → Creates distributable `.zip` in `build/distributions/`

### Dependency Management
- **BoxLang dependency**: Not in Maven Central yet. Looks for:
  1. `../../boxlang/build/libs/boxlang-{version}.jar` (local dev)
  2. `src/test/resources/libs/boxlang-{version}.jar` (downloaded via `downloadBoxLang` task)
- **ServiceLoader interfaces** registered in `build.gradle`:
  - `ortus.boxlang.runtime.bifs.BIF` (for BIFs)
  - `ortus.boxlang.runtime.components.Component` (for tags)
  - `ortus.boxlang.runtime.events.IInterceptor` (for interceptors)

### Version Scheme
- `gradle.properties` defines `version`, `boxlangVersion`, `jdkVersion`
- Branch-based versioning:
  - `development` branch → appends `-snapshot` suffix automatically
  - Release branches → uses `BUILD_ID` environment variable

## Code Style & Formatting

### Java
- **Eclipse formatter**: `.ortus-java-style.xml` (Ortus coding standard)
- **Spotless**: Enforced via `./gradlew spotlessApply`
- Tabs for indentation (4 spaces width), UTF-8 encoding

### BoxLang
- **CFFormat**: `.cfformat.json` configuration
  - Camel case for user-defined functions
  - CFDocs casing for built-in functions
  - Aligned assignments, properties, and params
  - No spaces in empty padding, spaces around operators

### EditorConfig
- Tab indentation (4-width) for `.java`, `.bx` files
- 2-space indentation for `.yml` files
- LF line endings, UTF-8, trim trailing whitespace

## Testing Strategy

### Java Unit Tests
- **Framework**: JUnit 5 + Truth assertions + Mockito
- **Test file**: `src/test/java/ortus/boxlang/websupport/IntegrationTest.java`
- **Pattern**: Load module manually, register with BoxLang runtime, execute BoxLang code
```java
ModuleRecord moduleRecord = new ModuleRecord( physicalPath );
moduleRecord.loadDescriptor(context).register(context).activate(context);
runtime.executeSource("/* BoxLang code */", context);
```

### Module Testing Notes
- Tests run against `build/module/` directory (not `src/`)
- Must build module structure before testing: `./gradlew shadowJar` → `createModuleStructure`
- Test classpath explicitly excludes `build/resources` to avoid conflicts

## Common Development Workflows

### Adding a New BIF
1. Create `src/main/bx/bifs/MyNewBif.bx`:
   ```boxlang
   @BoxBIF
   class {
       property name="moduleRecord";
       function invoke( required string arg ) {
           return "result";
       }
   }
   ```
2. Run `./gradlew shadowJar` (ServiceLoader auto-discovers and registers)
3. Build module: `./gradlew createModuleStructure`
4. Test: BIF name is camelCase of filename → `myNewBif()`

### Making Changes
1. Edit Java or BoxLang sources
2. `./gradlew spotlessApply` (format Java code)
3. `./gradlew shadowJar test` (compile and test)
4. Commit with descriptive message following Conventional Commits

### Release Process
- Versions managed via `gradle.properties`
- Bump tasks: `bumpMajorVersion`, `bumpMinorVersion`, `bumpPatchVersion`
- CI handles releases: GitHub Actions workflows in `.github/workflows/`

## Key Files to Know

- `build.gradle`: Complete build logic, dependency resolution, task definitions
- `box.json`: Module metadata for CommandBox/BoxLang package manager
- `ModuleConfig.bx`: Module settings (port, host, webroot, secure, requestKey)
- `MockHTTPExchange.java`: Core mock implementation with fluent setters

## Integration Points

- **BoxLang Runtime**: Module loaded by `ModuleService`, BIFs registered via ServiceLoader
- **Web Context**: Simulates `WebRequestBoxContext` for web-aware BoxLang execution
- **Request Scope**: Mock server instance stored at `request[settings.requestKey]` (default: "bxMockServer")

## Common Pitfalls

1. **Forgetting `downloadBoxLang`**: First-time build requires downloading BoxLang JAR
2. **Testing against `src/` instead of `build/module/`**: Tests load from compiled module
3. **Skipping `spotlessApply`**: CI will fail on unformatted Java code
4. **Not running `shadowJar` after Java changes**: ServiceLoader registration happens during compilation
5. **Token placeholders in source**: `@build.version@` etc. are replaced during build, not runtime

## Documentation References

- BoxLang Modules: https://boxlang.ortusbooks.com/essentials/modules
- Gradle ServiceLoader: https://github.com/harbby/gradle-serviceloader
- Project repo: https://github.com/ortus-boxlang/bx-web-support
