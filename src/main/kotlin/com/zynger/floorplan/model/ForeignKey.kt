package com.zynger.floorplan.model

import kotlinx.serialization.Serializable

@Serializable
data class ForeignKey (
    val table: String,
    val columns: List<String>,
    val referencedColumns: List<String>,
    val onDelete: String,
    val onUpdate: String
)