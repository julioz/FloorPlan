package com.zynger.floorplan.dbml

data class Index(val name: String, val columnNames: List<String>, val unique: Boolean = false){
    //(urn) [name:'index_TimeToLives_urn', unique]
}
