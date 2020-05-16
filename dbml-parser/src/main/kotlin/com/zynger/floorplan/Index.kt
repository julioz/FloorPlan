package com.zynger.floorplan

data class Index(val name: String, val columnNames: List<String>? = null, val unique: Boolean = false){
    // TODO should we enforce column names?
    //(urn) [name:'index_TimeToLives_urn', unique]
}
