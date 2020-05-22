package com.zynger.floorplan.room

import kotlinx.serialization.Serializable

@Serializable
data class Entity (
    val tableName: String,
    val createSql: String,
    val fields: List<Field>,
    val primaryKey: PrimaryKey,
    val indices: List<Index>,
    val foreignKeys: List<ForeignKey>
)
