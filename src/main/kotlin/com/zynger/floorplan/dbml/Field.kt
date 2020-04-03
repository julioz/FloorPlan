package com.zynger.floorplan.dbml

import com.zynger.floorplan.model.Field as DbField
import com.zynger.floorplan.model.PrimaryKey
import java.lang.IllegalArgumentException
import java.lang.StringBuilder

class Field(
    private val field: DbField,
    private val tablePrimaryKey: PrimaryKey
) {
    private val isPrimaryKey: Boolean
        get() = tablePrimaryKey.columnNames.any { it == this.field.columnName }

    private val type: String
        get() = this.field.affinity.toType()

    override fun toString(): String {
        return StringBuilder(field.columnName)
            .append(" ")
            .append(type)
            .apply {
                append(" ")
                append("[")
                if (isPrimaryKey) {
                    append("pk")
                    if (tablePrimaryKey.autoGenerate) {
                        append(", increment")
                    }
                    if (field.notNull) {
                        append(", not null")
                    }
                } else {
                    append("note: ")
                    append(if (field.notNull) "'not null'" else "'nullable'")
                }
                append("]")
            }
            .toString()
    }

    private fun String.toType(): String {
        return when (this) {
            "TEXT" -> "varchar"
            "INTEGER" -> "int"
            else -> throw IllegalArgumentException("Unrecognized affinity $this")

        }
    }
}
