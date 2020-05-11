Translate [Room](https://developer.android.com/topic/libraries/architecture/room) schemas to [Database Markup Language (DBML)](https://www.dbml.org/home/).

Once you have a JSON representation of your [Room exported schema](https://developer.android.com/training/data-storage/room/migrating-db-versions#export-schema), you can use `FloorPlan` to transform it into a DBML representation, to then be checked-in back into the repository or used as source for rendering through user interface.

![pipeline](images/json-dbml-pipeline.png)

[dbdiagram](https://dbdiagram.io/) is a free, simple tool to draw ER diagrams, that can be used to render the output of FloorPlan.

![screen capture](https://raw.githubusercontent.com/julioz/FloorPlan/master/docs/images/screencapture.gif)

For instance, when translating [Tivi](https://github.com/chrisbanes/tivi)'s [schema (v26)](https://github.com/chrisbanes/tivi/blob/master/data-android/schemas/app.tivi.data.TiviRoomDatabase/26.json), this is the rendered output in dbdiagram:

![Tivi model](https://raw.githubusercontent.com/julioz/FloorPlan/master/docs/images/Tivi26SchemaRender.png)

!!! warning "Note"
    FloorPlan and its developers are in no way associated to dbdiagram.
