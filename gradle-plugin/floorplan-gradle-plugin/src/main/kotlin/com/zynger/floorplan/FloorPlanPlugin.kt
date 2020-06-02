package com.zynger.floorplan

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import java.io.File
import java.lang.IllegalArgumentException

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
        ensureOnlyOneOutputFormatIsEnabled(this)
        return when (val output = getEnabledOutputFormats(this).first()) {
            is DbmlConfigurationExtension -> OutputFormat.DBML(
                DbmlConfiguration(output.creationSqlAsTableNote.get(), output.renderNullableFields.get())
            )
            is SvgConfigurationExtension -> OutputFormat.SVG
            is PngConfigurationExtension -> OutputFormat.PNG
            is DotConfigurationExtension -> OutputFormat.DOT
        }
    }

    private fun ensureOnlyOneOutputFormatIsEnabled(outputFormatExtension: OutputFormatExtension) {
        val enabled = getEnabledOutputFormats(outputFormatExtension)

        if (enabled.isEmpty()) {
            throw IllegalArgumentException("There are no enabled output formats.")
        }
        if (enabled.size != 1) {
            throw IllegalArgumentException("There can only be one enabled output format.")
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
}
