package com.zynger.floorplan.model

import kotlinx.serialization.Serializable

@Serializable
data class Schema(
    val formatVersion: Int,
    val database: Database
)
