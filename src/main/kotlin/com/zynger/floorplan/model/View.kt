package com.zynger.floorplan.model

import kotlinx.serialization.Serializable

@Serializable
data class View (
    val viewName: String,
    val createSql: String
)