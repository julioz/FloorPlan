package com.zynger.floorplan

data class Table(
    val name: String,
    val columns: List<Column>,
    val indexes: List<Index> = emptyList()
)