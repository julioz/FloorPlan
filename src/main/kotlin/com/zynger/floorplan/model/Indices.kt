package com.zynger.floorplan.model

import kotlinx.serialization.Serializable

@Serializable
data class Indices (
    val name: String,
    val unique: Boolean,
    val columnNames: List<String>,
    val createSql: String
)