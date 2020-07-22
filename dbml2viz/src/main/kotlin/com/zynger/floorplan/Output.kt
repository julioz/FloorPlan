package com.zynger.floorplan

import java.io.File

sealed class Format {
    abstract val extension: String
    data class DBML(val config: DbmlConfiguration): Format() {
        override val extension: String = "dbml"
    }

    object DOT: Format() {
        override val extension: String = "dot"
    }

    object SVG: Format() {
        override val extension: String = "svg"
    }

    object PNG: Format() {
        override val extension: String = "png"
    }
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
    val formats: List<Format>,
    val destination: Destination
)
