package com.zynger.floorplan.dbml

data class Column(
    val name: String,
    val type: String,
    val note: String? = null,
    val defaultValue: String? = null,
    val primaryKey: Boolean = false,
    val notNull: Boolean = false,
    val increment: Boolean = false,
    val reference: Reference? = null, // not null when this column references another through a column attribute,
    val rawValue: String = name
) {
    // example column: address varchar(255) [unique, not null, note: 'to include unit number']
}
