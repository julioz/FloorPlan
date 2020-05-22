package com.zynger.floorplan.room

import kotlinx.serialization.Serializable

@Serializable
data class Index (
    val name: String,
    val unique: Boolean,
    val columnNames: List<String>,
    val createSql: String
)
