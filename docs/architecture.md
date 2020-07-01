# Modularization

FloorPlan's source code is distributed across multiple modules, each responsible for one aspect of domain modeling or one stage of the processing pipeline.

![pipeline](images/floorplan-pipeline.svg)

## `floorplan-cli`

Home for the command line argument parsing logic, and general aspects of application bootstrapping.

The `cli` module is one possible interaction point between users and FloorPlan, but can be replaced by other user interfaces, like a Gradle plug-in or potentially a fully-fledged GUI application.

## [DBML models](https://github.com/julioz/FloorPlan/tree/master/dbml/src/main/kotlin/com/zynger/floorplan/dbml)

Barebones Kotlin models of the DBML object concepts.

This module has no logic in it other than housing the domain representations of Tables, References, Indexes, etc.

## Processing pipeline

### Consumer

This stage of the processing pipeline is responsible for translating the user input files into DBML models, representing each of the entities and relationships.

So far, FloorPlan processes:

- [Room](https://developer.android.com/topic/libraries/architecture/room) schemas, by deserializing their JSON representations (see [sample](https://github.com/julioz/FloorPlan/blob/master/samples/tivi-26.json)) on the [`:room-consumer`](https://github.com/julioz/FloorPlan/tree/master/room-consumer) module.
- [SQLite](https://www.sqlite.org/) `db` files, by connecting to them via a JDBC driver and query metadata of entities and relationships.
- [DBML](https://dbml.org/) schema files.

### Visualization

Depending on the output file format, FloorPlan uses different mechanisms to translate the input.

The DBML models are rendered as text by [a module of its own](https://github.com/julioz/FloorPlan/tree/master/dbml-render), while FloorPlan relies on [GraphViz visualization library](https://www.graphviz.org/) to render image representations.
