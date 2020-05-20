package com.zynger.floorplan.lex

import com.zynger.floorplan.dbml.Table
import org.intellij.lang.annotations.Language

object TableParser {
    @Language("RegExp") private const val TABLE_NAME = """(."\w+"|\w+.)"""
    @Language("RegExp") private const val TABLE_ALIAS = """(\s*as\s+[\w]+|\s*as\s+"[\w]+"|)"""
    @Language("RegExp") private const val TABLE_NOTES = """(\s\[.*]|)"""
    @Language("RegExp") private const val TABLE_CONTENT = """\{(\s|\n|[^}]*)}"""
    private val TABLE_REGEX = Regex("""[Tt]able\s+$TABLE_NAME\s*$TABLE_ALIAS$TABLE_NOTES\s*$TABLE_CONTENT""")

    fun parseTables(dbmlInput: String): List<Table> {
        // TODO: aliases and table notes also get parsed; should we update the modeling to include them?

        return TABLE_REGEX.findAll(dbmlInput).map {
            val tableName = it.groups[1]!!.value.trim()
            val tableContent = it.groups[4]!!.value.run {
                // TODO BUG, hack: the TABLE_REGEX doesn't take in account the Indexes block properly
                val indexesMatchResult = Regex("""Indexes\s+\{""").find(this)
                if (indexesMatchResult != null) {
                    this.substringBefore(indexesMatchResult.value)
                } else {
                    this
                }
            }
            Table(
                rawValue = it.groups[0]!!.value,
                name = tableName,
                columns = ColumnParser.parseColumns(tableName, tableContent),
                indexes = emptyList() // TODO include indexes parsing
            )
        }.toList()
    }
}
