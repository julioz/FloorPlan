package com.zynger.floorplan.gradle

import com.zynger.floorplan.*
import com.zynger.floorplan.dbml.Project
import com.zynger.floorplan.gradle.model.OutputFormat
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File

open class FloorPlanTask : DefaultTask() {

    @get:InputDirectory
    lateinit var schemaLocation: File

    @get:Nested
    lateinit var outputFormat: OutputFormat

    @OutputDirectory
    lateinit var outputLocation: File

    @TaskAction
    fun generateFloorPlan() {
        val schemas: List<File> = schemaLocation.findSchemas()
        if (schemas.isEmpty()) {
            logger.info("FloorPlan could not find any schema in specified location.")
        }

        schemas.forEach { schema ->
            val outputHandler = OutputParameterHandler(schema, schemaLocation)
            val outputFormat: Format = outputHandler.format(outputFormat)
            val outputFile: File = outputHandler.file(outputLocation, outputFormat)

            val project: Project = FloorPlanConsumerSniffer
                .sniff(schema)
                .read(schema)

            FloorPlan.render(
                project = project,
                output = Output(outputFormat, Destination.Disk(outputFile))
            )
        }
    }

    private fun File.findSchemas(): List<File> {
        return walk().filter { FloorPlanConsumerSniffer.isConsumable(it) }.toList()
    }
}
