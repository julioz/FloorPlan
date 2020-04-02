package com.zynger.floorplan.model

import kotlinx.serialization.Serializable

@Serializable
data class Views (
    val viewName: String,
    val createSql: String
)