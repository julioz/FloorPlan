# FloorPlan & Room

The [Room](https://developer.android.com/topic/libraries/architecture/room) persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.

Room can export your database's schema information into a `JSON` file at compile time.
To export the schema, set the `room.schemaLocation` annotation processor property (or `kapt`, if you use Kotlin) in your `build.gradle` file.

The `JSON` schemas can, then, be passed to FloorPlan as an input parameter for its rendering generation. Here we see an example with [FloorPlan's Gradle Plugin](../gradle-plugin.md):
```
android {
    ...
    defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += ["room.schemaLocation":
                             "$projectDir/schemas".toString()]
            }
        }
    }
}

...

floorPlan {
  schemaLocation = "$projectDir/schemas".toString()
  ...
}

```
