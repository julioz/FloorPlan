package com.zynger.floorplan

import com.zynger.floorplan.dbml.Table
import com.zynger.floorplan.model.*
import java.io.File
import kotlinx.serialization.json.*

fun main() {
    val src = File("samples/db.json")

    val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true, isLenient = true))
    val schema: Schema = json.parse(Schema.serializer(), src.readText())

    schema.database.entities.map { Table(it) }.forEach { println(it) }
}
