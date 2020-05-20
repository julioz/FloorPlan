package com.zynger.floorplan.dbml

data class Project(
    val tables: List<Table>,
    val references: List<Reference>
)
