package com.zynger.floorplan.lex

import com.zynger.floorplan.Column
import com.zynger.floorplan.Reference

object ColumnParser {
    private val COLUMN_REGEX = Regex("""("\w+"|\w+)+\s+("\w+"|\w+)(\s+\[[^]]*]|)[ ]*\n""")

    // TODO parse column default values

    fun parseColumns(tableName: String, columnsInput: String): List<Column> {
        return COLUMN_REGEX.findAll(columnsInput).map {
            val rawValue = it.groups[0]!!.value
            val name = it.groups[1]!!.value.removeSurroundQuotes()
            val type = it.groups[2]!!.value
            val columnProperties = it.groups[3]!!.value.trim()
            val notNull = columnProperties.contains("not null")
            val pk = columnProperties.contains("pk")
            val increment = columnProperties.contains("increment")
            val reference: Reference? = ColumnReferenceParser.parse(tableName, name, columnProperties)

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

    private fun String.removeSurroundQuotes(): String {
        return this.removeSurrounding("\"")
    }
}
