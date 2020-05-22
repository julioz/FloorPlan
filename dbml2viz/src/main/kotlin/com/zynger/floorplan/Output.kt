package com.zynger.floorplan

import java.io.File

enum class Format {
    DBML, DOT, SVG, PNG
}

sealed class Destination {
    data class Disk(val file: File): Destination()
    object StandardOut: Destination()
}

data class Output(
    val format: Format,
    val destination: Destination
)
