package com.zynger.floorplan.room

import kotlinx.serialization.Serializable

@Serializable
data class Field (
    val fieldPath: String,
    val columnName: String,
    val affinity: String,
    val notNull: Boolean,
    val defaultValue: String? = null
)
