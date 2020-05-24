package com.zynger.floorplan

import com.zynger.floorplan.dbml.Project
import com.zynger.floorplan.dbml.render.DbmlRenderer
import com.zynger.floorplan.room.RoomConsumer
import java.io.File

fun main(args: Array<String>) {
    hardcoded()

//    cmdline(args)
}

private fun hardcoded() {
    val src = File("samples/db-track-pol.json")
    val project: Project = RoomConsumer.read(src)
    val settings = Settings(false, false) // TODO
    println(DbmlRenderer.render(project, settings))
}

private fun cmdline(args: Array<String>) {
    val input = InputParser.parse(args)
    val src = File(input.schemaPath)
    val project: Project = RoomConsumer.read(src)

    val settings = Settings(input.creationSqlAsTableNote, input.renderNullableFields)
    val dbml = DbmlRenderer.render(project, settings)

    if (input.outputPath == null) {
        print(dbml)
    } else {
        val outputFile = File(input.outputPath)
        outputFile.parentFile.mkdirs()
        outputFile.writeText(dbml)
    }
}
