package com.zynger.floorplan

data class Column(
    val name: String,
    val type: String,
    val note: String? = null,
    val primaryKey: Boolean = false
) {
    // address varchar(255) [unique, not null, note: 'to include unit number']
}