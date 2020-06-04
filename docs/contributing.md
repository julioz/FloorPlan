# Contributing

Thank you for your interest in contributing to FloorPlan!

## Setup

* Check out the [project](https://github.com/julioz/FloorPlan/)
* Download [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) or [Android Studio](https://developer.android.com/studio) and import it.

## Build

* Build the CLI and its subprojects with `./gradlew :floorplan-cli:build`.
* Build the Gradle Plugin:
  * Publish a snapshot to your local maven repository: `./gradlew :floorplan-gradle-plugin:publishToMavenLocal`
  * Enable the sample app building: `echo floorplan.includeSampleApp=true >> ~/.gradle/gradle.properties`
  * (Optionally) Trigger the task on the sample app with `./gradlew :sample-android-project:generateFloorPlan`

!!! note "Note: Gradle Plugin - workflow"
    The gradle plugin development workflow requires republishing of the latest builds to `mavenLocal` after each code change. You will need to the deployment steps above if you want to manually test the changes in an end-to-end fashion with the help of the sample Android application.

## Tests

The project is unit tested and has integration tests for its Gradle plugin. These can be run via Gradle with:
```
gradlew test
```

You can also run only the Gradle Plugin tests by specifying that task explicitly:

```
gradlew :floorplan-gradle-plugin:test
```

## Deploying the docs locally

Install `mkdocs` and the required extensions:

```
pip install -r requirements.txt
```

Deploy it locally:

```
mkdocs serve
```
