package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.Settings
import com.zynger.floorplan.dbml.Column

class ColumnRenderer(
    private val column: Column,
    private val settings: Settings
) {
    fun render(): String {
        return StringBuilder(column.name)
            .append(" ")
            .append(column.type.toType())
            .apply {
                if (settings.renderNullableFields && column.nullable) {
                    append("(?)")
                }
            }
            .apply {
                append(" ")
                append("[")
                if (column.defaultValue != null) {
                    append("default:")
                    append(" ")
                    append("`")
                    append(column.defaultValue)
                    append("`")
                    append(", ")
                }
                if (column.primaryKey) {
                    append("pk")
                    if (column.increment) {
                        append(", increment")
                    }
                    if (column.notNull) {
                        append(", not null")
                    }
                } else {
                    append("note: ")
                    append(if (column.notNull) "'not null'" else "'nullable'")
                }
                append("]")
            }
            .toString()
    }

    private fun String.toType(): String {
        return when (this.toUpperCase().trim()) {
            "TEXT" -> "varchar"
            "INTEGER" -> "int"
            "REAL" -> "real"
            "BLOB" -> "blob"
            "NULL" -> "null"
            else -> {
                /**
                 * DBML supports all data types, as long as it is a single word.
                 * Examples: JSON, JSONB, decimal(1,2), etc.
                 *
                 * https://www.dbml.org/docs/#table-definition
                 */
                this.toLowerCase().replace("\\s".toRegex(), "")
            }
        }
    }

    private val Column.nullable: Boolean
        get() = !notNull
}
