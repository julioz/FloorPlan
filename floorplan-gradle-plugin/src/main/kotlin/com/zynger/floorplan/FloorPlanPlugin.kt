package com.zynger.floorplan

import org.gradle.api.Plugin
import org.gradle.api.Project

class FloorPlanPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.create("codeLines") {
            it.doLast {
                println("Hello from CodeLinesCounterPlugin")
            }
        }.apply {
            group = "stat"
        }
    }
}
