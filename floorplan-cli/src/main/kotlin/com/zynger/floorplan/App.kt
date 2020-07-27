package com.zynger.floorplan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.path
import com.zynger.floorplan.dbml.Project
import java.lang.IllegalArgumentException

class FloorPlanCli: CliktCommand(
    name = "floorplan",
    help = "Render SCHEMAPATH as DBML or ER diagram."
) {
    private val onlyDbmlNote = "[note: only for DBML outputs]"
    private val validFormats = listOf("dbml", "svg", "png", "dot")
    private val schemaFile by argument().file(
        mustExist = true,
        canBeFile = true,
        canBeDir = false
    )
    private val outputPath by option(
        names = *arrayOf("-o", "--output"),
        help = "Output file for the rendering content"
    ).path(
        mustExist = false,
        canBeFile = true,
        canBeDir = false
    )
    private val formats by option(
        names = *arrayOf("--format", "-f"),
        help = "Specify an output format for the rendering content [one or more of DBML, SVG, PNG, DOT]"
    ).split(",").validate {
        require(it.all { format -> validFormats.contains(format) }) { "Unrecognized rendering format: $it. Valid ones are ${validFormats.joinToString()}." }
    }
    private val creationSqlAsTableNote by option(
        "--creation-sql-as-table-note",
        help = "Adds the SQL used to create tables as notes $onlyDbmlNote"
    ).flag(default = false)
    private val renderNullableFields by option(
        "--render-nullable-fields",
        help = "Changes the rendering of the data type of nullable fields $onlyDbmlNote"
    ).flag(default = false)

    override fun run() {
        val project: Project = FloorPlanConsumerSniffer
            .sniff(schemaFile)
            .read(schemaFile)

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
            FloorPlan.render(
                project = project,
                output = Output(
                    outputFormat,
                    if (outputPath == null) Destination.StandardOut else Destination.Disk(outputPath!!.toFile())
                )
            )
        }
    }
}

fun main(args: Array<String>) = FloorPlanCli().main(args)
