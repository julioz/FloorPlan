package com.zynger.floorplan

import com.zynger.floorplan.model.Table
import java.io.File
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Data(val a: Int, val b: String = "42")

fun main() {
    val src = File("samples/db.json")
    val table = Table("Users")
    println("Hello World $table")


}