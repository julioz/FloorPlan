package com.zynger.floorplan

import com.zynger.floorplan.model.Schema
import java.io.File
import kotlinx.serialization.json.*

fun main() {
    val src = File("samples/db.json")

    val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true, isLenient = true))
    val schema: Schema = json.parse(Schema.serializer(), src.readText())

    println(schema)

}