# FloorPlan as an IntelliJ Plugin

FloorPlan has a (incubating) plug-in for IDEs based on the [IntelliJ platform](https://www.jetbrains.com/products.html) (IDEA, Android Studio, AppCode, CLion, etc.)

The plug-in will scan the file system for FloorPlan's output files and display them as a gutter action in the IDEA sidebar for extra discoverability and easy access.

![FloorPlan IntelliJ plug-in](images/floorplan-intellij-plugin.gif)

## Download and install

Given the incubating nature of the plugin, it is not distributed through [JetBrains plug-in repository](https://plugins.jetbrains.com/).
Download the plug-in `zip` file and [follow the 'instalation from disk' steps](https://www.jetbrains.com/help/idea/managing-plugins.html#install_plugin_from_disk).

## Building

The source code is available in the [`:floorplan-intellij-plugin` module](https://github.com/julioz/FloorPlan/tree/master/floorplan-intellij-plugin). It relies on Gradle and the `intellij` DevKit to manipulate the PSI tree.

Deploy an artifact for local debugging with `./gradlew runIde`.

For manual distribution or local installation, invoke `gradlew buildPlugin` target to create the plugin distribution. The resulting `JAR`/`ZIP` is located in `build/distributions` and can then be [installed](https://www.jetbrains.com/help/idea/managing-plugins.html#install_plugin_from_disk).
 