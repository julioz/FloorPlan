package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.dbml.Column

class ColumnRenderer(private val column: Column) {
    fun render(): String {
        return "Column ${column.name}"
    }
}