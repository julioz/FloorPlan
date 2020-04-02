package com.zynger.floorplan.model

import kotlinx.serialization.Serializable

@Serializable
data class Entities (
    val tableName: String,
    val createSql: String,
    val fields: List<Fields>,
    val primaryKey: PrimaryKey,
    val indices: List<Indices>,
    val foreignKeys: List<ForeignKey>
)