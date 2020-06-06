package com.zynger.floorplan

import com.zynger.floorplan.gradle.model.DbmlConfiguration
import com.zynger.floorplan.gradle.FloorPlanTask
import com.zynger.floorplan.gradle.model.OutputFormat
import com.zynger.floorplan.gradle.extension.*
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
                    task.group = TASK_GROUP
                    task.description = "Translates database schemas into DBML or ER diagrams."

                    check(floorPlanExtension.schemaLocation.isPresent) { "Missing schemaLocation property." }
                    check(floorPlanExtension.outputLocation.isPresent) { "Missing outputLocation property." }

                    task.schemaLocation = File(floorPlanExtension.schemaLocation.get())
                    task.outputLocation = File(floorPlanExtension.outputLocation.get())
                    task.outputFormat = outputFormatExtension.getFormat()
                }
        }
    }

    private fun OutputFormatExtension.getFormat(): OutputFormat {
        val outputFormats = getEnabledOutputFormats(this)
        check(outputFormats.isNotEmpty()) { "There are no enabled output formats." }
        check(outputFormats.size == 1) { "There can only be one enabled output format." }

        return when (val output = outputFormats.first()) {
            is DbmlConfigurationExtension -> OutputFormat.DBML(
                DbmlConfiguration(
                    output.creationSqlAsTableNote.get(),
                    output.renderNullableFields.get()
                )
            )
            is SvgConfigurationExtension -> OutputFormat.SVG
            is PngConfigurationExtension -> OutputFormat.PNG
            is DotConfigurationExtension -> OutputFormat.DOT
        }
    }

    private fun getEnabledOutputFormats(outputFormatExtension: OutputFormatExtension): List<OutputFormatConfiguration> {
        val outputFormats = listOf(
            outputFormatExtension.dbmlConfiguration,
            outputFormatExtension.svgConfiguration,
            outputFormatExtension.pngConfiguration,
            outputFormatExtension.dotConfiguration
        )
        return outputFormats.associateWith { it.enabled.get() }.filter { it.value }.map { it.key }
    }

    companion object {
        const val TASK_GROUP = "FloorPlan"
    }
}
