package com.zynger.floorplan.lex

import com.zynger.floorplan.dbml.Index
import org.intellij.lang.annotations.Language

object IndexParser {

    val INDEXES_BLOCK_REGEX = Regex("""[Ii]ndexes\s+\{(\s|\n|[^}]+)}""")

    @Language("RegExp") private const val INDEX_NAME = """\((.+)\)"""
    @Language("RegExp") private const val INDEX_PROPERTIES = """\[([^\]]+)]"""

    private val INDEX_REGEX = Regex("""$INDEX_NAME.*$INDEX_PROPERTIES\s*\n""")
    private val INDEX_NAME_REGEX = Regex("""name:\s*['"](\w+)['"]""")
    private const val UNNAMED_INDEX = "unnamed_index"

    fun parseIndexes(indexContent: String): List<Index> {
        return INDEXES_BLOCK_REGEX.find(indexContent)?.let {
            INDEX_REGEX.findAll(it.groups[1]!!.value).toList().map { indexMatch ->
                val indexColumns = indexMatch.groups[1]!!.value.split(",").map { columnName -> columnName.trim() }
                val indexProperties = indexMatch.groups[2]!!.value

                val indexNameMatch = INDEX_NAME_REGEX.find(indexProperties)
                val indexName = if (indexNameMatch != null) {
                    indexNameMatch.groups[1]!!.value
                } else {
                    UNNAMED_INDEX
                }
                Index(
                    name = indexName,
                    columnNames = indexColumns,
                    unique = indexProperties.contains("unique", ignoreCase = true)
                )
            }
        } ?: emptyList()
    }
}
