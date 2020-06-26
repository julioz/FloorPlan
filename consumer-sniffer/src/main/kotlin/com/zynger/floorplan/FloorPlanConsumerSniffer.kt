package com.zynger.floorplan

import com.zynger.floorplan.room.RoomConsumer
import com.zynger.floorplan.sqlite.SqliteConsumer
import java.io.File

object FloorPlanConsumerSniffer {

    fun sniff(inputSourceFile: File): Consumer {
        return when (inputSourceFile.extension) {
            "json" -> RoomConsumer
            "db" -> SqliteConsumer
            else -> throw IllegalArgumentException("Unknown file extension: ${inputSourceFile.extension}")
        }
    }

    fun isConsumable(inputSourceFile: File): Boolean {
        return inputSourceFile.extension == "json" || inputSourceFile.extension == "db"
    }
}
