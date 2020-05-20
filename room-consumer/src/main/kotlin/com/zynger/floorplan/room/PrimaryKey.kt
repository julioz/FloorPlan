package com.zynger.floorplan.room

import kotlinx.serialization.Serializable

@Serializable
data class PrimaryKey (
    val columnNames: List<String>,
    val autoGenerate: Boolean
)
