package com.zynger.floorplan.gradle

import com.zynger.floorplan.Destination
import com.zynger.floorplan.FloorPlan
import com.zynger.floorplan.Format
import com.zynger.floorplan.Output
import com.zynger.floorplan.dbml.Project
import com.zynger.floorplan.DbmlConfiguration
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
            val outputFormat = outputFormat.mapOutputFormat()
            val outputFileName = "${schema.nameWithoutExtension}.${outputFormat.extension}"
            val outputFile = File(getOutputFileParentDirectory(schema), outputFileName)

            FloorPlan.render(
                project = project,
                output = Output(outputFormat, Destination.Disk(outputFile))
            )
        }
    }

    private fun File.findRoomSchemas(): List<File> {
        return walk().filter { it.extension == "json" }.toList()
    }

    private fun getOutputFileParentDirectory(schema: File): File {
        val relativizedPathToSchemaInput = schemaLocation.toURI().relativize(schema.toURI()).path
        return if (relativizedPathToSchemaInput.contains(File.separator)) {
            // schema is stored under a sub-directory,
            // we must relativize the directory structure to point to the parent dir.
            val relativizedPath = relativizedPathToSchemaInput.substringBeforeLast(File.separator)
            File(outputLocation.path + File.separator + relativizedPath)
        } else {
            // schema is directly under [outputLocation], so the parent is [outputLocation] itself.
            outputLocation
        }
    }

    private fun OutputFormat.mapOutputFormat(): Format {
        return when (this) {
            is OutputFormat.DBML -> Format.DBML(
                DbmlConfiguration(
                    this.config.creationSqlAsTableNote,
                    this.config.renderNullableFields
                )
            )
            OutputFormat.DOT -> Format.DOT
            OutputFormat.SVG -> Format.SVG
            OutputFormat.PNG -> Format.PNG
        }
    }
}
