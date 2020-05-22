package com.zynger.floorplan

import com.zynger.floorplan.dbml.Table
import com.zynger.floorplan.room.Database
import com.zynger.floorplan.room.RoomConsumer
import java.io.File

fun main(args: Array<String>) {
    val input = InputParser.parse(args)
    val src = File(input.schemaPath)
    val database: Database = RoomConsumer.read(src)

    val settings = Settings(input.creationSqlAsTableNote, input.renderNullableFields)
    val dbml = database
        .entities
        .map { Table(it, settings) }
        .joinToString(separator = "\n\n")

    if (input.outputPath == null) {
        print(dbml)
    } else {
        val outputFile = File(input.outputPath)
        outputFile.parentFile.mkdirs()
        outputFile.writeText(dbml)
    }
}
