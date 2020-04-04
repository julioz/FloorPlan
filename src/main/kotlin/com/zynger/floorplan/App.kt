package com.zynger.floorplan

import com.zynger.floorplan.dbml.Table
import com.zynger.floorplan.model.Schema
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File

fun main(args: Array<String>) {
    val input = InputParser.parse(args)
    val src = File(input.schemaPath)
    val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true, isLenient = true))

    val dbml = json
        .parse(Schema.serializer(), src.readText())
        .database
        .entities
        .map { Table(it) }
        .joinToString(separator = "\n\n")
    print(dbml)
}
