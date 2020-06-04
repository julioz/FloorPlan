package com.zynger.floorplan

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class FloorPlanGradlePluginIntegrationTest {

    @get:Rule
    var testProjectRoot = TemporaryFolder()

    private fun writeBuildGradle(build: String) {
        testProjectRoot.newFile("build.gradle").run { writeText(build) }
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
}
