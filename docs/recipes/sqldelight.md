# FloorPlan & SQLDelight

In order to use [SQLDelight](https://github.com/cashapp/sqldelight) schemas as input to FloorPlan, make sure you are on a recent version of library (>=1.0).

When applying SQLDelight's Gradle plugin, specify the following gradle extension to configure it to output `.db` files.

```
sqldelight {
    <databaseName> {
        packageName = "<fully qualified package name to where the database is generated>"
        schemaOutputDirectory = file("<output directory for the db file>")
    }
}
```

Once done, running `./gradlew <module>:tasks` will uncover a [generateSchema](https://github.com/cashapp/sqldelight/blob/master/sqldelight-gradle-plugin/src/main/kotlin/com/squareup/sqldelight/gradle/GenerateSchemaTask.kt) gradle task per build type (usually `debug`, `release`, etc.).

```
./gradlew generate<buildType><projectName>Schema
``` 

will output a `.db` file can be used as an input to `FloorPlan`, via its [CLI](../run.md) or [Gradle Plugin](../gradle-plugin.md) interfaces.

!!! note
    Read more about [SQLDelight's](https://github.com/cashapp/sqldelight) configuration [on the project's website](https://cashapp.github.io/sqldelight/gradle/).