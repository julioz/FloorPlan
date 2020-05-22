package com.zynger.floorplan.dbml

data class Index(val name: String, val columnNames: List<String>? = null, val unique: Boolean = false){
    // TODO should we enforce that indexes must have column names? as to make the property not nullable
    //(urn) [name:'index_TimeToLives_urn', unique]
}
