package com.zynger.floorplan

import com.zynger.floorplan.dbml.Table
import com.zynger.floorplan.model.*
import java.io.File
import kotlinx.serialization.json.*

fun main() {
    val src = File("samples/db.json")
    val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true, isLenient = true))

    val dbml = json
        .parse(Schema.serializer(), src.readText())
        .database
        .entities
        .map { Table(it) }
        .joinToString(separator = "\n\n")
    print(dbml)
}
