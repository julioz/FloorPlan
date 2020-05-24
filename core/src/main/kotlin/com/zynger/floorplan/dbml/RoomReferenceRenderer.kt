package com.zynger.floorplan.dbml

import com.zynger.floorplan.room.ForeignKey
import com.zynger.floorplan.room.ForeignKeyAction
import java.lang.StringBuilder

class RoomReferenceRenderer(
    private val fromTable: String,
    private val foreignKey: ForeignKey
) {

    override fun toString(): String {
        // Ref: U.country_code > countries.code
        // > many-to-one; < one-to-many; - one-to-one

        val fromColumn = foreignKey.columns.first()
        val toTable = foreignKey.table
        val toColumn = foreignKey.referencedColumns.first()
        val deleteAction = foreignKey.onDelete.toDbml()
        val updateAction = foreignKey.onUpdate.toDbml()
        return StringBuilder("Ref: ")
            .append(fromTable)
            .append(".")
            .append(fromColumn)
            .append(" - ")
            .append(toTable)
            .append(".")
            .append(toColumn)
            .append(" [")
            .append("delete: ")
            .append(deleteAction)
            .append(", ")
            .append("update: ")
            .append(updateAction)
            .append("]")
            .toString()
    }

    private fun ForeignKeyAction.toDbml(): String {
        return when (this) {
            ForeignKeyAction.NO_ACTION -> "no action"
            ForeignKeyAction.RESTRICT -> "restrict"
            ForeignKeyAction.SET_NULL -> "set null"
            ForeignKeyAction.SET_DEFAULT -> "set default"
            ForeignKeyAction.CASCADE -> "cascade"
        }
    }
}
