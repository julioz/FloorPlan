package com.zynger.floorplan

import com.zynger.floorplan.dbml.DbmlConsumer
import com.zynger.floorplan.room.RoomConsumer
import com.zynger.floorplan.sqlite.SqliteConsumer
import java.io.File

object FloorPlanConsumerSniffer {

    private val extensionToConsumer = mapOf(
        "json" to RoomConsumer,
        "db" to SqliteConsumer,
        "dbml" to DbmlConsumer
    )

    fun sniff(inputSourceFile: File): Consumer =
        if (isConsumable(inputSourceFile)) {
            extensionToConsumer.getValue(inputSourceFile.extension)
        } else {
            throw IllegalArgumentException("Unknown file extension: ${inputSourceFile.extension}")
        }

    fun isConsumable(inputSourceFile: File): Boolean = extensionToConsumer.containsKey(inputSourceFile.extension)
}
