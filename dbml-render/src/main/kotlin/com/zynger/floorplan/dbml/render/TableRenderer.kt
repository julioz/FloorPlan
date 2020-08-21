package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.Settings
import com.zynger.floorplan.dbml.Reference
import com.zynger.floorplan.dbml.Table

class TableRenderer(
    private val table: Table,
    referencesFromTable: List<Reference>,
    private val settings: Settings
) {

    private val columnRenderers: List<ColumnRenderer> = table.columns.map { ColumnRenderer(it, settings) }
    private val indexRenderers: List<IndexRenderer> = table.indexes.map { IndexRenderer(it) }
    private val referenceRenderers: List<ReferenceRenderer> = referencesFromTable.map { ReferenceRenderer(it) }

    fun render() : String {
        return StringBuilder()
            .append("Table ${table.name}")
            .append(" ")
            .apply {
                if (table.alias != null) {
                    append("as ${table.alias} ")
                }
            }
            .apply {
                if (table.note != null) {
                    append("[note: '${table.note}'] ")
                }
            }
            .append("{")
            .appendLine()
            .apply {
                append(columnRenderers.joinToString("\n") { it.render().prependIndent("  ") })
            }
            .appendLine()
            .apply {
                if (this@TableRenderer.indexRenderers.isNotEmpty()) {
                    appendLine()
                    appendIndicesBlock()
                }
            }
            .apply {
                if (settings.creationSqlAsTableNote) {
                    appendLine("  ")
                    append("Note: ".prependIndent("  "))
                    append("'")
                    append(table.rawValue)
                    append("'")
                    appendLine()
                }
            }
            .append("}")
            .apply {
                if (this@TableRenderer.referenceRenderers.isNotEmpty()) {
                    appendLine()
                    appendLine()
                    append(referenceRenderers.joinToString("\n") { it.render() })
                }
            }
            .toString()
    }

    private fun StringBuilder.appendIndicesBlock() {
        append("  ")
        append("Indexes")
        append("  ")
        appendLine("{")
        this@TableRenderer.indexRenderers.forEach { appendLine(it.render().prependIndent("    ")) }
        append("  ")
        appendLine("}")
    }
}
