package com.zynger.floorplan.room

import kotlinx.serialization.Serializable

@Serializable
data class View (
    val viewName: String,
    val createSql: String
)