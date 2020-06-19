package com.zynger.floorplan

import com.zynger.floorplan.dbml.Project
import com.zynger.floorplan.room.RoomConsumer
import com.zynger.floorplan.sqlite.SqliteConsumer
import java.io.File
import java.lang.IllegalArgumentException

fun main(args: Array<String>) {
    val input = InputParser.parse(args)

    val src = File(input.schemaPath)

    val project: Project = when (src.extension) {
        "json" -> RoomConsumer.read(src)
        "db" -> SqliteConsumer.read(src)
        else -> throw IllegalArgumentException("Unknown file extension: ${src.extension}")
    }

    FloorPlan.render(
        project = project,
        output = Output(
            input.mapOutputFormat(),
            if (input.outputPath == null) Destination.StandardOut else Destination.Disk(File(input.outputPath))
        )
    )
}

private fun InputParser.Input.mapOutputFormat(): Format {
    return format?.let {
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
    } ?: Format.DBML(
        DbmlConfiguration(
            creationSqlAsTableNote,
            renderNullableFields
        )
    )
}
