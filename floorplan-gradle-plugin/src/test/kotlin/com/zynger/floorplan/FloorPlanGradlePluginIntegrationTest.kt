package com.zynger.floorplan

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class FloorPlanGradlePluginIntegrationTest {

    @get:Rule
    var testProjectRoot = TemporaryFolder()

    private fun writeBuildGradle(build: String) {
        testProjectRoot.newFile("build.gradle").run { writeText(build) }
    }
    private fun createSchemasDirectory() {
        testProjectRoot.newFolder("schemas")
    }

    @Test
    fun applyingBarePluginSucceeds() {
        writeBuildGradle(
            """plugins {
             |  id "com.zynger.floorplan"
             |}""".trimMargin()
        )
        GradleRunner.create()
            .withProjectDir(testProjectRoot.root)
            .withPluginClasspath()
            .build()
    }

    @Test
    fun testMissingSchemaLocationFailsBuild() {
        writeBuildGradle(
            """plugins {
             |  id "com.zynger.floorplan"
             |}
             |
             |floorPlan {
             |  outputLocation = "${"\$projectDir"}/schemas"
             |  outputFormat {
             |    svg {
             |      enabled = true
             |    }
             |  }
             |}""".trimMargin()
        )
        floorPlanRunner()
            .buildAndFail()
            .withFailureMessage("Missing schemaLocation property.")
    }

    @Test
    fun testMissingOutputLocationFailsBuild() {
        writeBuildGradle(
            """plugins {
             |  id "com.zynger.floorplan"
             |}
             |
             |floorPlan {
             |  schemaLocation = "${"\$projectDir"}/schemas"
             |  outputFormat {
             |    svg {
             |      enabled = true
             |    }
             |  }
             |}""".trimMargin()
        )
        floorPlanRunner()
            .buildAndFail()
            .withFailureMessage("Missing outputLocation property.")
    }

    @Test
    fun testMissingOutputFormatFailsBuild() {
        createSchemasDirectory()
        writeBuildGradle(
            """plugins {
             |  id "com.zynger.floorplan"
             |}
             |
             |floorPlan {
             |  schemaLocation = "${"\$projectDir"}/schemas"
             |  outputLocation = "${"\$projectDir"}/schemas"
             |}""".trimMargin()
        )
        floorPlanRunner()
            .buildAndFail()
            .withFailureMessage("There are no enabled output formats.")
    }

    @Test
    fun testEmptyOutputFormatFailsBuild() {
        createSchemasDirectory()
        writeBuildGradle(
            """plugins {
             |  id "com.zynger.floorplan"
             |}
             |
             |floorPlan {
             |  schemaLocation = "${"\$projectDir"}/schemas"
             |  outputLocation = "${"\$projectDir"}/schemas"
             |  outputFormat {
             |  }
             |}""".trimMargin()
        )
        floorPlanRunner()
            .buildAndFail()
            .withFailureMessage("There are no enabled output formats.")
    }

    @Test
    fun testAllDisabledOutputFormatFailsBuild() {
        createSchemasDirectory()
        writeBuildGradle(
            """plugins {
             |  id "com.zynger.floorplan"
             |}
             |
             |floorPlan {
             |  schemaLocation = "${"\$projectDir"}/schemas"
             |  outputLocation = "${"\$projectDir"}/schemas"
             |  outputFormat {
             |    dbml {
             |      enabled = false
             |    }
             |    svg {
             |      enabled = false
             |    }
             |  }
             |}""".trimMargin()
        )
        floorPlanRunner()
            .buildAndFail()
            .withFailureMessage("There are no enabled output formats.")
    }

    @Test
    fun testMultipleEnabledOutputFormatFailsBuild() {
        createSchemasDirectory()
        writeBuildGradle(
            """plugins {
             |  id "com.zynger.floorplan"
             |}
             |
             |floorPlan {
             |  schemaLocation = "${"\$projectDir"}/schemas"
             |  outputLocation = "${"\$projectDir"}/schemas"
             |  outputFormat {
             |    dbml {
             |      enabled = true
             |    }
             |    svg {
             |      enabled = true
             |    }
             |  }
             |}""".trimMargin()
        )
        floorPlanRunner()
            .buildAndFail()
            .withFailureMessage("There can only be one enabled output format.")
    }

    @Test
    fun testSingleOutputFormatEnabled() {
        createSchemasDirectory()
        writeBuildGradle(
            """plugins {
             |  id "com.zynger.floorplan"
             |}
             |
             |floorPlan {
             |  schemaLocation = "${"\$projectDir"}/schemas"
             |  outputLocation = "${"\$projectDir"}/schemas"
             |  outputFormat {
             |    svg {
             |      enabled = true
             |    }
             |  }
             |}""".trimMargin()
        )
        floorPlanRunner()
            .build()
            .withSuccessfulMessage()
    }

    @Test
    fun testMultipleOutputFormatButSingleEnabled() {
        createSchemasDirectory()
        writeBuildGradle(
            """plugins {
             |  id "com.zynger.floorplan"
             |}
             |
             |floorPlan {
             |  schemaLocation = "${"\$projectDir"}/schemas"
             |  outputLocation = "${"\$projectDir"}/schemas"
             |  outputFormat {
             |    dbml {
             |      enabled = false
             |    }
             |    svg {
             |      enabled = true
             |    }
             |  }
             |}""".trimMargin()
        )
        floorPlanRunner()
            .build()
            .withSuccessfulMessage()
    }

    @Ignore("Unsupported feature. To be worked on.")
    @Test
    fun testSingleOutputFormatEnabledWithCollapsibleStatement() {
        createSchemasDirectory()
        writeBuildGradle(
            """plugins {
             |  id "com.zynger.floorplan"
             |}
             |
             |floorPlan {
             |  schemaLocation = "${"\$projectDir"}/schemas"
             |  outputLocation = "${"\$projectDir"}/schemas"
             |  outputFormat {
             |    svg.enabled = true
             |  }
             |}""".trimMargin()
        )
        floorPlanRunner()
            .build()
            .withSuccessfulMessage()
    }

    private fun floorPlanRunner(): GradleRunner {
        return GradleRunner.create()
            .withProjectDir(testProjectRoot.root)
            .withPluginClasspath()
            .withArguments(GRADLE_TASK_NAME)
    }

    private fun BuildResult.withSuccessfulMessage(): BuildResult {
        assertThat(output).contains(BUILD_SUCCESSFUL)
        return this
    }

    private fun BuildResult.withFailureMessage(message: String): BuildResult {
        assertThat(output).contains(message)
        return this
    }

    private companion object {
        private const val GRADLE_TASK_NAME = "generateFloorPlan"
        private const val BUILD_SUCCESSFUL = "BUILD SUCCESSFUL"
    }
}
