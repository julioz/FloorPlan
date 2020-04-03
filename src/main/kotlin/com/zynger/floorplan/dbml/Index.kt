package com.zynger.floorplan.dbml

import java.lang.StringBuilder
import com.zynger.floorplan.model.Index as DbIndex

class Index(
    private val index: DbIndex
) {

    override fun toString(): String {
        //    (merchant_id, status) [name:'product_status']
        //    id [unique]
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
