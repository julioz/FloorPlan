package com.zynger.floorplan

import java.io.File

sealed class Format {
    data class DBML(val config: DbmlConfiguration): Format()
    object DOT: Format()
    object SVG: Format()
    object PNG: Format()
}

data class DbmlConfiguration(
    val creationSqlAsTableNote: Boolean = false,
    val renderNullableFields: Boolean = false
)

sealed class Destination {
    data class Disk(val file: File): Destination()
    object StandardOut: Destination()
}

data class Output(
    val format: Format,
    val destination: Destination
)
