package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.dbml.Index
import java.lang.StringBuilder

class IndexRenderer(
    private val index: Index
) {
    fun render(): String {
        return StringBuilder()
            .append("(")
            .append(index.columnNames.joinToString(","))
            .append(")")
            .append(" ")
            .append("[")
            .append("name:")
            .append("'${index.name}'")
            .apply {
                if (index.unique) {
                    append(", unique")
                }
            }
            .append("]")
            .toString()
    }
}
