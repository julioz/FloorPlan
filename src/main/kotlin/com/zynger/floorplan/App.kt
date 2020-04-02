package com.zynger.floorplan

import com.zynger.floorplan.model.Table
import java.io.File

fun main() {
    val src = File("samples/db.json")
    val table = Table("Users")
    println("Hello World $table")

    src.forEachLine {
        println(it)
    }
}