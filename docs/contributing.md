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

## Release

* Create a local release branch from `master`
```
git checkout master
git pull
git checkout -b release_<next-release-version>
```

* Update `floorPlanVersion` in the [`versioning.gradle`](https://github.com/julioz/FloorPlan/blob/master/versioning.gradle) (remove `-SNAPSHOT`)
```gradle
floorPlanVersion = "<next-release-version>"
```

* Update `docs/changelog.md` after checking out all changes:
  - https://github.com/julioz/FloorPlan/compare/v`<current-release-version>`...master

* Take one last look
```
git diff
```

* Commit all local changes
```
git commit -am "Prepare <next-release-version> release."
```

* Perform a clean build
```
./gradlew clean
./gradlew build
```

* Create a tag and push it
```
git tag v<next-release-version>
git push origin v<next-release-version>
```

* Create a [new GitHub release based on that tag](https://github.com/julioz/FloorPlan/releases/new), and upload the zip and tar distributions

* Make sure you have valid credentials in `~/.gradle/gradle.properties` to upload the artifacts
```
SONATYPE_NEXUS_USERNAME=
SONATYPE_NEXUS_PASSWORD=
```

* Upload the artifacts to Sonatype OSS Nexus
```
./gradlew publish --no-daemon --no-parallel
```

* Release to Maven Central
  - Login to [Sonatype OSS Nexus](https://oss.sonatype.org/)
  - Click on **Staging Repositories**
  - Scroll to the bottom, you should see an entry named `comjuliozynger-XXXX`
  - Check the box next to the `comjuliozynger-XXXX` entry, click **Close** then **Confirm**
  - Wait a bit, hit **Refresh**, until the *Status* for that column changes to *Closed*.
  - Check the box next to the `comjuliozynger-XXXX` entry, click **Release** then **Confirm**

* Release to Gradle Plugin Portal
```
./gradlew publishPlugins
```

* Release via homebrew
```
scripts/release_homebrew.sh
```

* Merge the release branch to master
```
git checkout master
git pull
git merge --no-ff release_<next-release-version>
```
* Update `floorPlanVersion` in `versioning.gradle` (increase version and add `-SNAPSHOT`)
```gradle
floorPlanVersion = "REPLACE_WITH_NEXT_VERSION_NUMBER-SNAPSHOT"
```

* Commit your changes
```
git commit -am "Prepare for next development iteration."
```

* Push your changes
```
git push
```
