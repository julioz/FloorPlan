package com.zynger.floorplan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.zynger.floorplan.dbml.Project
import java.io.File
import java.lang.IllegalArgumentException

class FloorPlanCli: CliktCommand(
    name = "floorplan",
    help = "Render SCHEMAPATH as DBML or ER diagram."
) {
    private val onlyDbmlNote = "[note: only for DBML outputs]"
    private val schemaPath by argument().file(
        mustExist = true,
        canBeFile = true,
        canBeDir = false
    )
    private val outputPath by option(
        names = *arrayOf("-o", "--output"),
        help = "Output file for the rendering content"
    ).file(
        mustExist = false,
        canBeFile = true,
        canBeDir = false
    )
    private val creationSqlAsTableNote by option(
        "--creation-sql-as-table-note",
        help = "Adds the SQL used to create tables as notes $onlyDbmlNote"
    ).flag(default = false)
    private val renderNullableFields by option(
        "--render-nullable-fields",
        help = "Changes the rendering of the data type of nullable fields $onlyDbmlNote"
    ).flag(default = false)

    override fun run() {
        echo("Hello $schemaPath! Outputing to $outputPath, creationAsNote = $creationSqlAsTableNote, renderNullableFields = $renderNullableFields")
    }
}

fun main(args: Array<String>) = FloorPlanCli().main(args)

/*fun main(args: Array<String>) {
    val input = InputParser.parse(args)

    val src = File(input.schemaPath)

    val project: Project = FloorPlanConsumerSniffer
        .sniff(src)
        .read(src)

    val outputFormats = input.mapOutputFormats()

    outputFormats.forEach { outputFormat ->
        FloorPlan.render(
            project = project,
            output = Output(
                outputFormat,
                if (input.outputPath == null) Destination.StandardOut else Destination.Disk(File(input.outputPath))
            )
        )
    }
}*/

private fun InputParser.Input.mapOutputFormats(): List<Format> {
    return formats?.map {
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
}
