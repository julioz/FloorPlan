package com.zynger.floorplan

import com.zynger.floorplan.dbml.Project
import com.zynger.floorplan.room.RoomConsumer
import java.io.File

fun main(args: Array<String>) {
    val input = InputParser.parse(args)

    val src = File(input.schemaPath)
    val project: Project = RoomConsumer.read(src)

    FloorPlan.render(
        project = project,
        output = Output(
            Format.DBML(
                DbmlConfiguration(
                    input.creationSqlAsTableNote,
                    input.renderNullableFields
                )
            ),
            Destination.StandardOut
        )
    )
}
