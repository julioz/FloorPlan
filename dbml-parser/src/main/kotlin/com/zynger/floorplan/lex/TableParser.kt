package com.zynger.floorplan.lex

import com.zynger.floorplan.dbml.Table
import org.intellij.lang.annotations.Language
import java.lang.StringBuilder

object TableParser {
    @Language("RegExp") private const val TABLE_NAME = """("\w+"|\w+)"""
    @Language("RegExp") private const val TABLE_ALIAS = """(\s*as\s+[\w]+|\s*as\s+"[\w]+"|)"""
    @Language("RegExp") private const val TABLE_NOTES = """(\s\[.*]|)"""
    @Language("RegExp") private const val TABLE_CONTENT = """\{(\s|\n|[^}]*}*)\s*}"""
    private val TABLE_REGEX = Regex("""[Tt]able\s+$TABLE_NAME\s*$TABLE_ALIAS$TABLE_NOTES\s*$TABLE_CONTENT""")

    fun parseTables(dbmlInput: String): List<Table> {
        return TABLE_REGEX.findAll(dbmlInput).map {
            val tableName = it.groups[1]!!.value.trim().removeSurroundQuotes()
            val tableAlias = it.groups[2]?.parseTableAlias()
            val tableNoteFromTitle = it.groups[3]?.let { matchGroup -> parseTableNote(matchGroup.value) }
            val tableContent = it.groups[4]!!.value
            val indexes = IndexParser.parseIndexes(tableContent)
            val primaryKeysFromIndexes = IndexParser.parseCompositePrimaryKeys(tableContent)
            val tableNoteFromBody: String? = parseTableNoteFromBody(tableContent)
            val tableNote = tableNoteFromBody ?: tableNoteFromTitle

            val postProcessedTableContent = tableContent.removeIndexesBlock().removeTableNotes()
            Table(
                rawValue = it.groups[0]!!.value,
                name = tableName,
                alias = tableAlias,
                note = tableNote,
                columns = ColumnParser.parseColumns(tableName, postProcessedTableContent, primaryKeysFromIndexes),
                indexes = indexes
            )
        }.toList()
    }

    private fun MatchGroup.parseTableAlias(): String? {
        return value.substringAfter("as").trim().removeSurroundQuotes().takeIf { it.isNotBlank() }
    }

    private fun parseTableNoteFromBody(tableContent: String): String? {
        return tableContent.getTableNotes()
            .mapNotNull { parseTableNote(it) }
            .lastOrNull()
    }

    private fun parseTableNote(value: String): String? {
        val noteDelimiter = "note: '"
        val noteStartIndex = value.indexOf(noteDelimiter, ignoreCase = true)
        return if (noteStartIndex == -1) {
            null
        } else {
            value
                .substring(noteStartIndex + noteDelimiter.length, value.length)
                .substringBefore("'")
                .trim()
                .removeSurroundQuotes()
                .takeIf { it.isNotBlank() }
        }
    }

    private fun String.removeSurroundQuotes(): String {
        return this.removeSurrounding("\"")
    }

    private fun String.removeTableNotes(): String {
        val tableNotes = getTableNotes()
        return buildString {
            this@removeTableNotes.split(System.lineSeparator())
                .map { it.trim() }
                .filter { it !in tableNotes }
                .forEach {
                    appendln(it)
                }
        }
    }

    private fun String.getTableNotes(): List<String> {
        return split(System.lineSeparator())
            .map { it.trim() }
            .filter { it.startsWith("note:", ignoreCase = true) }
    }

    private fun String.removeIndexesBlock(): String {
        val indexesGroup = IndexParser.INDEXES_BLOCK_REGEX.find(this)
        return if (indexesGroup != null) {
            this.remove(indexesGroup.groups.first()!!.value)
        } else {
            this
        }
    }

    private fun String.remove(substring: String): String {
        return replace(substring, "")
    }
}
