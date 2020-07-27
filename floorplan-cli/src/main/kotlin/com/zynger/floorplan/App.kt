package com.zynger.floorplan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import com.zynger.floorplan.dbml.Project
import java.io.File
import java.lang.IllegalArgumentException

class FloorPlanCli: CliktCommand(
    name = "floorplan",
    help = "Render SCHEMAPATH as DBML or ER diagram."
) {
    private val schemaPath by argument().file(
        mustExist = true,
        canBeFile = true,
        canBeDir = false
    )
    override fun run() {
        echo("Hello $schemaPath!")
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
