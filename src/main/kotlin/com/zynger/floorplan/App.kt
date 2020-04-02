package com.zynger.floorplan

import com.zynger.floorplan.model.Table
import java.io.File
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Schema(val formatVersion: Int)

fun main() {
    val src = File("samples/db.json")
    val table = Table("Users")
    println("Hello World $table")

    val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
    val obj = json.parse(Schema.serializer(), src.readText())

    println(obj)

}