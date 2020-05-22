package com.zynger.floorplan.room

import kotlinx.serialization.Serializable

@Serializable
data class Schema(
    val formatVersion: Int,
    val database: Database
)
