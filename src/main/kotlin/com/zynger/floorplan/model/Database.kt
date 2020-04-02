package com.zynger.floorplan.model

import kotlinx.serialization.Serializable

@Serializable
data class Database(
    val version: Int,
    val identityHash: String,
    val entities: List<Entities>,
    val views: List<Views>,
    val setupQueries: List<String>
)