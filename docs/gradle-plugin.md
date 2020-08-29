# FloorPlan as a Gradle Plugin

### Apply the plugin

Apply the [gradle plugin](https://plugins.gradle.org/plugin/com.juliozynger.floorplan) in your root `build.gradle` file:

```
buildscript {
  dependencies {
    classpath "com.juliozynger.floorplan:floorplan-gradle-plugin:<version>"
  }
}
```

and in the modules you want `FloorPlan`'s Gradle task to exist.

For example, in an Android application module:

```
plugins {
    id 'com.android.application' // could be library or any other module
    id 'com.juliozynger.floorplan'
}
```

### Gradle extension

Configure FloorPlan's Gradle extension by definining a `floorPlan` block:

```
floorPlan {
    schemaLocation = "$projectDir/schemas".toString()
    outputLocation = "$projectDir/floorplan-output".toString()
    outputFormat {
        svg {
            enabled = true
        }
    }
}
```

#### Output formats

FloorPlan is able to output different file formats.

Take a look at what a full configuration would look like, defining `DBML` and `SVG` as output formats (and providing [extra configurations for `DBML`](../run/#output-format)):

```
floorPlan {
    schemaLocation = "$projectDir/schemas".toString()
    outputLocation = "$projectDir/floorplan-output".toString()
    outputFormat {
        dbml {
            enabled = true
            creationSqlAsTableNote = false
            renderNullableFields = false
        }
        svg {
            enabled = true
        }
        png {
            enabled = false
        }
        dot {
            enabled = false
        }
    }
}
```

#### ERD Notation

FloorPlan supports rendering entity-relationship diagrams using different notations.

```
floorPlan {
    schemaLocation = "$projectDir/schemas".toString()
    outputLocation = "$projectDir/floorplan-output".toString()
    notation = "crowsfoot"
    outputFormat {
        svg {
            enabled = true
        }
    }
}
```

Check [this page](../run/#supported-notations) for the supported notations list.

### Generate Floor Plan

Once everything is setup, you can finally run FloorPlan to translate database schemas into ER diagrams.

For example, to generate the diagrams associated to the `sample-android-project` module schemas, we can run:

```
./gradlew :sample-android-project:generateFloorPlan
```