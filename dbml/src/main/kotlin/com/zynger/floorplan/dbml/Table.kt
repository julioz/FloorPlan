package com.zynger.floorplan.dbml

data class Table(
    val rawValue: String,
    val name: String,
    val columns: List<Column>,
    val indexes: List<Index> = emptyList()
)
