package com.zynger.floorplan.lex

import com.zynger.floorplan.Column
import com.zynger.floorplan.Reference
import com.zynger.floorplan.ReferenceOrder

object ColumnParser {
    private val COLUMN_REGEX = Regex("""("\w+"|\w+)+\s+("\w+"|\w+)(\s+\[[^]]*]|)[ ]*\n""")
    private val COLUMN_REFERENCE_REGEX = Regex("""((ref)\s*:\s*([<>-])\s*("\w+"|\w+)\.("\w+"|\w+))""")

    fun parseColumns(tableName: String, columnsInput: String): List<Column> {
        return COLUMN_REGEX.findAll(columnsInput).map {
            val rawValue = it.groups[0]!!.value
            val name = it.groups[1]!!.value
            val type = it.groups[2]!!.value
            val columnProperties = it.groups[3]!!.value.trim()
            val notNull = columnProperties.contains("not null")
            val pk = columnProperties.contains("pk")
            val increment = columnProperties.contains("increment")
            val reference: Reference? = if (COLUMN_REFERENCE_REGEX.containsMatchIn(columnProperties)) {
                val referenceProperties = COLUMN_REFERENCE_REGEX.find(columnProperties)!!
                Reference(
                    rawValue = referenceProperties.groups[0]!!.value,
                    fromTable = tableName,
                    fromColumn = name,
                    referenceOrder = ReferenceOrder.fromString(referenceProperties.groups[3]!!.value),
                    toTable = referenceProperties.groups[4]!!.value,
                    toColumn = referenceProperties.groups[5]!!.value
                )
            } else null

            Column(
                rawValue = rawValue,
                name = name,
                type = type,
                primaryKey = pk,
                notNull = notNull,
                increment = increment,
                reference = reference
            )
        }.toList()
    }
}
