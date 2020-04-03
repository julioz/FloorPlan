# FloorPlan

Translate [Room](https://developer.android.com/topic/libraries/architecture/room) schemas to [Database Markup Language (DBML)](https://www.dbml.org/home/).

Once you a JSON representation of your [Room exported schema](https://developer.android.com/training/data-storage/room/migrating-db-versions#export-schema), you can use `FloorPlan` to transform it into a DBML representation, to be then checked-in back into the repository or used as source for rendering through user interface.

[dbdiagram](https://dbdiagram.io/) is a free, simple tool to draw ER diagrams, that can be used to render the output of FloorPlan.

![screen capture](https://raw.githubusercontent.com/julioz/FloorPlan/master/docs/screencapture.gif)

### Run FloorPlan

After checking out this repository, make sure you have Gradle installed and run:
```
gradlew run --args=<path-to-schema-file>
```

#### Tests

The project is unit tested. These can be run via Gradle with:
```
gradlew test
```

