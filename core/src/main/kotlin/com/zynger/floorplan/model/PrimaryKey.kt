package com.zynger.floorplan.model

import kotlinx.serialization.Serializable

@Serializable
data class PrimaryKey (
    val columnNames: List<String>,
    val autoGenerate: Boolean
)