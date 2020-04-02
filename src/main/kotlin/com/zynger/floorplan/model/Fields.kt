package com.zynger.floorplan.model

import kotlinx.serialization.Serializable

@Serializable
data class Fields (
    val fieldPath: String,
    val columnName: String,
    val affinity: String,
    val notNull: Boolean
)