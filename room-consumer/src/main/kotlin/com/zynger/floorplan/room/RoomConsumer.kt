package com.zynger.floorplan.room

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File

object RoomConsumer {
    private val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true, isLenient = true))

    // TODO return DBML's plain `Project` model
    fun read(src: File): Database {
        return json
            .parse(Schema.serializer(), src.readText())
            .database
    }
}
