# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

* * *

## [Unreleased]

### Fixed

- Removed by accident the web support libraries when upgrading to 1.1.0

## [1.1.0] - 2025-12-05

### Added

- New BIF to get a fluent MockHTTPExchange builder: `mockRequestNew()`
- Mocking of web request context with current calling context as parent so all BIFS work as expected
- Updated all internal versions
- Updated dependencies to latest versions
- Updated all github actions

## [1.0.0] - 2025-07-13

- First iteration of this module

[unreleased]: https://github.com/ortus-boxlang/bx-web-support/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/ortus-boxlang/bx-web-support/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/ortus-boxlang/bx-web-support/compare/8c46ecb2e48389888c2d3200aa20435b4d3d8b1e...v1.0.0
