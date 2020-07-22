package com.zynger.floorplan.gradle

import com.zynger.floorplan.DbmlConfiguration
import com.zynger.floorplan.Format
import com.zynger.floorplan.gradle.model.OutputFormat
import java.io.File

class OutputParameterHandler(
    private val schema: File,
    private val schemaLocation: File
) {

    fun formats(outputFormats: List<OutputFormat>): List<Format> {
        return outputFormats.map { it.mapOutputFormat() }
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

    fun file(outputLocation: File, outputFormat: Format): File {
        val outputFileName = "${schema.nameWithoutExtension}.${outputFormat.extension}"
        return File(getOutputFileParentDirectory(outputLocation), outputFileName)
    }

    private fun getOutputFileParentDirectory(outputLocation: File): File {
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
}
