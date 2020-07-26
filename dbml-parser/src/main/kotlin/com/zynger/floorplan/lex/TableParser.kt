package com.zynger.floorplan.lex

import com.zynger.floorplan.dbml.Table
import org.intellij.lang.annotations.Language

object TableParser {
    @Language("RegExp") private const val TABLE_NAME = """("\w+"|\w+)"""
    @Language("RegExp") private const val TABLE_ALIAS = """(\s*as\s+[\w]+|\s*as\s+"[\w]+"|)"""
    @Language("RegExp") private const val TABLE_NOTES = """(\s\[.*]|)"""
    @Language("RegExp") private const val TABLE_CONTENT = """\{(\s|\n|[^}]*}*)\s*}"""
    private val TABLE_REGEX = Regex("""[Tt]able\s+$TABLE_NAME\s*$TABLE_ALIAS$TABLE_NOTES\s*$TABLE_CONTENT""")

    fun parseTables(dbmlInput: String): List<Table> {
        // TODO: aliases and table notes also get parsed; should we update the modeling to include them?

        return TABLE_REGEX.findAll(dbmlInput).map {
            val tableName = it.groups[1]!!.value.trim().removeSurroundQuotes()
            val tableContent = it.groups[4]!!.value
            val indexes = IndexParser.parseIndexes(tableContent)
            val primaryKeysFromIndexes = IndexParser.parseCompositePrimaryKeys(tableContent)

            Table(
                rawValue = it.groups[0]!!.value,
                name = tableName,
                columns = ColumnParser.parseColumns(tableName, removeIndexes(tableContent), primaryKeysFromIndexes),
                indexes = indexes
            )
        }.toList()
    }

    private fun String.removeSurroundQuotes(): String {
        return this.removeSurrounding("\"")
    }

    private fun removeIndexes(tableContent: String): String {
        val indexesGroup = IndexParser.INDEXES_BLOCK_REGEX.find(tableContent)
        return if (indexesGroup != null) {
            tableContent.remove(indexesGroup.groups.first()!!.value)
        } else {
            tableContent
        }
    }

    private fun String.remove(substring: String): String {
        return replace(substring, "")
    }
}
