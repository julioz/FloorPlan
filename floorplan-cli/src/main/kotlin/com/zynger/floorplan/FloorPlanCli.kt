package com.zynger.floorplan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.path
import com.zynger.floorplan.dbml.Project
import java.io.File
import java.util.*

class FloorPlanCli: CliktCommand(
    name = "floorplan",
    help = "Render SCHEMAPATH as DBML or ER diagram."
) {
    init {
        val properties = Properties().apply {
            Thread.currentThread().contextClassLoader
                .getResource("floorplan-cli-version.properties")!!
                .openStream()
                .use { load(it) }
        }
        val version = properties["floorPlanVersion"].toString()
        versionOption(version)
    }

    private val onlyDbmlNote = "[note: only for DBML outputs]"
    private val validFormats = listOf("dbml", "svg", "png", "dot")
    private val validNotation = Notation.all.map { it.identifier }
    private val schemaFile by argument().file(
        mustExist = true,
        canBeFile = true,
        canBeDir = false
    )
    private val outputPath by option(
        names = *arrayOf("-o", "--output"),
        help = "Output directory for the rendering content"
    ).path(
        mustExist = false,
        canBeFile = false,
        canBeDir = true
    )
    private val formats by option(
        names = *arrayOf("--format", "-f"),
        help = "Specify an output format for the rendering content [one or more of DBML, SVG, PNG, DOT]"
    ).split(",").validate {
        require(it.all { format -> validFormats.contains(format) }) {
            "Unrecognized rendering format: ${it.minus(validFormats)}. Valid ones are ${validFormats.joinToString()}."
        }
    }
    private val notation by option(
        names = *arrayOf("--notation", "-n"),
        help = "Specify the notation for the relationship rendering in the diagram [one of ${validNotation.joinToString()}]"
    ).validate {
        require(validNotation.contains(it)) {
            "Unrecognized notation: $it. Valid ones are ${validNotation.joinToString()}."
        }
    }
    private val creationSqlAsTableNote by option(
        "--creation-sql-as-table-note", "--ctn",
        help = "Adds the SQL used to create tables as notes $onlyDbmlNote"
    ).flag(default = false)
    private val renderNullableFields by option(
        "--render-nullable-fields", "--rnf",
        help = "Changes the rendering of the data type of nullable fields $onlyDbmlNote"
    ).flag(default = false)

    override fun run() {
        val project: Project = FloorPlanConsumerSniffer
            .sniff(schemaFile)
            .read(schemaFile)

        val notation: Notation = if (notation == null) {
            Notation.Chen
        } else {
            Notation.all.find { notation == it.identifier }!!
        }

        val outputFormats = formats?.map {
            when (it.trim().toLowerCase()) {
                "dbml" -> Format.DBML(
                    DbmlConfiguration(
                        creationSqlAsTableNote,
                        renderNullableFields
                    )
                )
                "svg" -> Format.SVG
                "png" -> Format.PNG
                "dot" -> Format.DOT
                else -> throw IllegalArgumentException("Unrecognized rendering format: $it. Must be one of dbml, svg, png, dot.")
            }
        } ?: listOf(
            Format.DBML(
                DbmlConfiguration(
                    creationSqlAsTableNote,
                    renderNullableFields
                )
            )
        )

        outputFormats.forEach { outputFormat ->
            val destination = if (outputPath == null) {
                Destination.StandardOut
            } else {
                val outputFileName = "${schemaFile.nameWithoutExtension}.${outputFormat.extension}"
                val outputFile = File(
                    getOutputFileParentDirectory(
                        schema = schemaFile,
                        outputLocation = outputPath!!.toFile()
                    ),
                    outputFileName
                )
                Destination.Disk(outputFile)
            }

            FloorPlan.render(
                project = project,
                output = Output(
                    format = outputFormat,
                    notation = notation,
                    destination = destination
                )
            )
        }
    }

    private fun getOutputFileParentDirectory(schema: File, outputLocation: File): File {
        val relativizedPathToSchemaInput = schema.parentFile?.toURI()?.relativize(schema.toURI())?.path
        return if (relativizedPathToSchemaInput != null && relativizedPathToSchemaInput.contains(File.separator)) {
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

fun main(args: Array<String>) = FloorPlanCli().main(args)
