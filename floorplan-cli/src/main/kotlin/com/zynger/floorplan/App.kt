package com.zynger.floorplan

import com.zynger.floorplan.dbml.Project
import com.zynger.floorplan.dbml.render.ProjectRenderer
import com.zynger.floorplan.room.RoomConsumer
import java.io.File

fun main(args: Array<String>) {
    val input = InputParser.parse(args)

    val src = File(input.schemaPath)
    val project: Project = RoomConsumer.read(src)
    val settings = Settings(input.creationSqlAsTableNote, input.renderNullableFields)

    val dbml = ProjectRenderer.render(project, settings)

    if (input.outputPath == null) {
        print(dbml)
    } else {
        val outputFile = File(input.outputPath)
        outputFile.parentFile.mkdirs()
        outputFile.writeText(dbml)
    }
}
