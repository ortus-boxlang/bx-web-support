package ortus.boxlang.websupport;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.scopes.Key;

/**
 * This loads the module and runs an integration test on the module.
 */
public class IntegrationTest extends BaseIntegrationTest {

	@DisplayName( "Test the module loads in BoxLang" )
	@Test
	public void testModuleLoads() {

		// Then
		assertThat( moduleService.getRegistry().containsKey( moduleName ) ).isTrue();

		// @formatter:off
		runtime.executeSource(
		    """
			// Testing code here
			""",
		    context
		);
		// @formatter:on

		// Asserts here

	}

	@DisplayName( "Test mockServerGet creates server instance" )
	@Test
	public void testmockServerGet() {
		// @formatter:off
		runtime.executeSource(
		    """
			// Get a mock server instance
			mockServer = mockServerGet();

			// Store results for assertions
			result = {
				"mockServerExists": isDefined( "mockServer" ),
				"host": mockServer.getRequestServerName(),
				"port": mockServer.getRequestServerPort()
			};
			""",
		    context
		);
		// @formatter:on

		// Verify the mock server was created
		var resultStruct = variables.getAsStruct( result );
		assertThat( resultStruct.getAsBoolean( Key.of( "mockServerExists" ) ) ).isTrue();
		assertThat( resultStruct.getAsString( Key.of( "host" ) ) ).isEqualTo( "localhost" );
		assertThat( resultStruct.getAsInteger( Key.of( "port" ) ) ).isEqualTo( 8080 );
	}

	@DisplayName( "Test mockServerGet caching behavior" )
	@Test
	public void testmockServerGetCaching() {
		// @formatter:off
		runtime.executeSource(
		    """
			// Get a mock server instance
			mockServer1 = mockServerGet();
			server1Id = mockServer1.hashCode();

			// Get the same instance again (should be cached)
			mockServer2 = mockServerGet();
			server2Id = mockServer2.hashCode();

			// Force create a new one
			mockServer3 = mockServerGet( force: true );
			server3Id = mockServer3.hashCode();

			result = {
				"serversAreSame": server1Id == server2Id,
				"server3IsDifferent": server1Id != server3Id
			};
			""",
		    context
		);
		// @formatter:on

		// Verify the mock server caching behavior
		var resultStruct = variables.getAsStruct( result );
		assertThat( resultStruct.getAsBoolean( Key.of( "serversAreSame" ) ) ).isTrue();
		assertThat( resultStruct.getAsBoolean( Key.of( "server3IsDifferent" ) ) ).isTrue();
	}

	@DisplayName( "Test mockServerGet with custom settings" )
	@Test
	public void testmockServerGetWithCustomSettings() {
		// @formatter:off
		runtime.executeSource(
		    """
			// Create mock server with custom settings
			mockServer = mockServerGet(
				host: "example.com",
				port: 9090,
				secure: true,
				force: true
			);

			result = {
				"host": mockServer.getRequestServerName(),
				"port": mockServer.getRequestServerPort(),
				"secure": mockServer.isRequestSecure()
			};
			""",
		    context
		);
		// @formatter:on

		// Verify custom settings were applied
		var resultStruct = variables.getAsStruct( result );
		assertThat( resultStruct.getAsString( Key.of( "host" ) ) ).isEqualTo( "example.com" );
		assertThat( resultStruct.getAsInteger( Key.of( "port" ) ) ).isEqualTo( 9090 );
		assertThat( resultStruct.getAsBoolean( Key.of( "secure" ) ) ).isTrue();
	}

	@DisplayName( "Test mock server with headers and content using fluent API" )
	@Test
	public void testMockServerWithHeadersAndContent() {
		// @formatter:off
		runtime.executeSource(
		    """
			// Create mock server and set up request data using fluent API
			mockServer = mockServerGet( force: true )
				.setRequestMethod( "POST" )
				.setRequestContentType( "application/json" )
				.setRequestBody( '{"test": "data", "value": 123}' )
				.addRequestHeader( "X-Custom-Header", "CustomValue" )
				.addRequestHeader( "Authorization", "Bearer token123" );

			// Now use getHTTPRequestData() to access the request data
			httpData = getHTTPRequestData();

			result = {
				"method": httpData.method,
				"content": httpData.content,
				"headers": httpData.headers,
				"hasCustomHeader": httpData.headers.keyExists( "X-Custom-Header" ),
				"customHeaderValue": httpData.headers["X-Custom-Header"] ?: "",
				"hasAuthHeader": httpData.headers.keyExists( "Authorization" ),
				"authHeaderValue": httpData.headers["Authorization"] ?: ""
			};
			""",
		    context
		);
		// @formatter:on

		// Verify the mock server was configured correctly
		var resultStruct = variables.getAsStruct( result );
		assertThat( resultStruct.getAsString( Key.of( "method" ) ) ).isEqualTo( "POST" );
		assertThat( resultStruct.getAsString( Key.of( "content" ) ) ).contains( "test" );
		assertThat( resultStruct.getAsString( Key.of( "content" ) ) ).contains( "data" );
		assertThat( resultStruct.getAsBoolean( Key.of( "hasCustomHeader" ) ) ).isTrue();
		assertThat( resultStruct.getAsString( Key.of( "customHeaderValue" ) ) ).isEqualTo( "CustomValue" );
		assertThat( resultStruct.getAsBoolean( Key.of( "hasAuthHeader" ) ) ).isTrue();
		assertThat( resultStruct.getAsString( Key.of( "authHeaderValue" ) ) ).isEqualTo( "Bearer token123" );
	}

	@DisplayName( "Test a mockRequestNew() and execute it to verify request processing" )
	@Test
	public void testMockRequestNewAndExecute() {
		// @formatter:off
		runtime.executeSource(
		    """
			// Create a new mock request
			mockServer = mockRequestNew(
				method: "PUT",
				path: "/api/test",
				headers: {
					"Content-Type": "application/json",
					"X-Test-Header": "TestValue"
				},
				body: '{"name": "BoxLang", "type": "test"}'
			).execute()

			// Retrieve the processed request data
			httpData = getHTTPRequestData();

			result = {
				"method": httpData.method,
				"url": mockServer.getRequestURL(),
				"body": httpData.content,
				"hasTestHeader": httpData.headers.keyExists( "X-Test-Header" ),
				"testHeaderValue": httpData.headers["X-Test-Header"] ?: ""
			};
			""",
		    context
		);
		// @formatter:on

		// Verify the executed request data
		var resultStruct = variables.getAsStruct( result );
		assertThat( resultStruct.getAsString( Key.of( "method" ) ) ).isEqualTo( "PUT" );
		assertThat( resultStruct.getAsString( Key.of( "url" ) ) ).isEqualTo( "/api/test" );
		assertThat( resultStruct.getAsString( Key.of( "body" ) ) ).contains( "BoxLang" );
		assertThat( resultStruct.getAsString( Key.of( "body" ) ) ).contains( "test" );
		assertThat( resultStruct.getAsBoolean( Key.of( "hasTestHeader" ) ) ).isTrue();
		assertThat( resultStruct.getAsString( Key.of( "testHeaderValue" ) ) ).isEqualTo( "TestValue" );
	}

}
