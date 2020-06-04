package com.zynger.floorplan.gradle.model

sealed class OutputFormat {
    data class DBML(val config: DbmlConfiguration): OutputFormat()
    object DOT: OutputFormat()
    object SVG: OutputFormat()
    object PNG: OutputFormat()
}

data class DbmlConfiguration(
    val creationSqlAsTableNote: Boolean = false,
    val renderNullableFields: Boolean = false
)
