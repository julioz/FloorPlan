package com.zynger.floorplan.gradle

import com.zynger.floorplan.*
import com.zynger.floorplan.dbml.Project
import com.zynger.floorplan.gradle.model.OutputFormat
import com.zynger.floorplan.room.RoomConsumer
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
        val schemas: List<File> = schemaLocation.findRoomSchemas()
        schemas.forEach { schema ->
            val project: Project = RoomConsumer.read(schema)
            val outputHandler = OutputParameterHandler(schema, schemaLocation)
            val outputFormat: Format = outputHandler.format(outputFormat)
            val outputFile: File = outputHandler.file(outputLocation, outputFormat)

            FloorPlan.render(
                project = project,
                output = Output(outputFormat, Destination.Disk(outputFile))
            )
        }
    }

    private fun File.findRoomSchemas(): List<File> {
        return walk().filter { it.extension == "json" }.toList()
    }
}
