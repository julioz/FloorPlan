package com.zynger.floorplan.dbml

import com.zynger.floorplan.model.ForeignKey
import java.lang.StringBuilder

class Reference(
    private val fromTable: String,
    private val foreignKey: ForeignKey
) {

    override fun toString(): String {
        // Ref: U.country_code > countries.code
        // > many-to-one; < one-to-many; - one-to-one

        val fromColumn = foreignKey.columns.first()
        val toTable = foreignKey.table
        val toColumn = foreignKey.referencedColumns.first()
        return StringBuilder("Ref: ")
            .append(fromTable)
            .append(".")
            .append(fromColumn)
            .append(" - ")
            .append(toTable)
            .append(".")
            .append(toColumn)
            .toString()
    }
}