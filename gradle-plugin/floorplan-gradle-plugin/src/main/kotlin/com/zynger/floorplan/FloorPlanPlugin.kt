package com.zynger.floorplan

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import java.io.File

class FloorPlanPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        val floorPlanExtension = project.extensions.create("floorPlan", FloorPlanExtension::class.java)
        floorPlanExtension as ExtensionAware
        val outputFormatExtension = floorPlanExtension.extensions.create("outputFormat", OutputFormatExtension::class.java)

        project.afterEvaluate {
            project.tasks
                .register("generateFloorPlan", FloorPlanTask::class.java)
                .configure { task ->
                    task.schemaLocation = File(floorPlanExtension.schemaLocation)
                    task.outputLocation = File(floorPlanExtension.outputLocation)
                    task.outputFormat = outputFormatExtension.getFormat()
                }
        }
    }

    private fun OutputFormatExtension.getFormat(): OutputFormat {
        println(this)
        return OutputFormat.SVG
    }
}
