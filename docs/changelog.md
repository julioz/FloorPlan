# Changelog

## 0.3

- FloorPlan engine: Allow for [multi-format output](https://github.com/julioz/FloorPlan/pull/49)
  - A user can now specify a list of output formats for FloorPlan to output to.
- FloorPlan engine: [Crow's Foot notation](https://github.com/julioz/FloorPlan/pull/58)
- CLI: Specify a [directory for output](https://github.com/julioz/FloorPlan/pull/57) instead of file
  - With multi-format output, the CLI will now require an output directory in its argument list to write all renderings into.
- CLI: Rewrite using [Clikt](https://github.com/ajalt/clikt/)
  - Follows argument parsing standards
  - Allows for multiple option definition for argument parsing
  - Help and Version commands
- Parser: Parse multi-column primary key and [composite indexes](https://github.com/julioz/FloorPlan/pull/50)
- Parser: Include [table notes and table aliases](https://github.com/julioz/FloorPlan/pull/53) in output
- General: Update to [Kotlin 1.4](https://github.com/julioz/FloorPlan/pull/55)

## 0.2

- Add SQLite-based consumer, with driver to connect to `.db` files.
  - We now allow for rendering schemas for popular SQLite ORMs, such as SQLDelight and Core Data.
- Add DBML consumer, to allow for rendering schemas directly from `.dbml` files.
- `:dbml-parser` now parses `Index`es.

## 0.1

Initial release.
