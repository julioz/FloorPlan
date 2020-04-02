package com.zynger.floorplan.model

import kotlinx.serialization.Serializable

@Serializable
data class Field (
    val fieldPath: String,
    val columnName: String,
    val affinity: String,
    val notNull: Boolean
)