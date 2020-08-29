package com.zynger.floorplan.gradle

import com.zynger.floorplan.*
import com.zynger.floorplan.dbml.Project
import com.zynger.floorplan.gradle.model.OutputFormat
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File
import java.lang.IllegalArgumentException

open class FloorPlanTask : DefaultTask() {

    @get:InputDirectory
    lateinit var schemaLocation: File

    @get:Nested
    lateinit var outputFormats: List<OutputFormat>

    @OutputDirectory
    lateinit var outputLocation: File

    @get:Input @Optional
    var notation: String? = null

    @TaskAction
    fun generateFloorPlan() {
        if (!schemaLocation.exists()) {
            throw IllegalArgumentException("FloorPlan could not find the directory to search for the schemas: ${schemaLocation.absolutePath}")
        }

        val schemas: List<File> = schemaLocation.findSchemas()

        if (schemas.isEmpty()) {
            logger.info("FloorPlan could not find any schema in specified location ${schemaLocation.absolutePath}")
        }

        schemas.forEach { schema ->
            val outputHandler = OutputParameterHandler(schema, schemaLocation)
            val outputFormats: List<Format> = outputHandler.formats(outputFormats)
            val notation: Notation = Notation.all.find { it.identifier == notation } ?: Notation.Chen

            val project: Project = FloorPlanConsumerSniffer
                .sniff(schema)
                .read(schema)

            outputFormats.forEach { outputFormat ->
                val outputFile: File = outputHandler.file(outputLocation, outputFormat)

                FloorPlan.render(
                    project = project,
                    output = Output(
                        format = outputFormat,
                        notation = notation,
                        destination = Destination.Disk(outputFile)
                    )
                )
            }
        }
    }

    private fun File.findSchemas(): List<File> {
        return walk().filter { FloorPlanConsumerSniffer.isConsumable(it) }.toList()
    }
}
