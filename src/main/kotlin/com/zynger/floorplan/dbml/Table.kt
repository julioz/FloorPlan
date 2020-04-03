package com.zynger.floorplan.dbml

import com.zynger.floorplan.model.Entity

class Table(
    entity: Entity
) {
    private val tableName: String = entity.tableName
    private val fields: List<Field> = entity.fields.map { Field(it, entity.primaryKey) }
    private val indices: List<Index> = entity.indices.map { Index(it) }
    private val references: List<Reference> = entity.foreignKeys.map { Reference(tableName, it) }

    override fun toString(): String {
        return StringBuilder()
            .append("Table $tableName")
            .append(" ")
            .append("{")
            .appendln()
            .apply {
                append(fields.joinToString("\n") { it.toString().prependIndent("  ") })
            }
            .appendln()
            .apply {
                if (this@Table.indices.isNotEmpty()) {
                    appendln()
                    appendIndicesBlock()
                }
            }
            .append("}")
            .apply {
                if (this@Table.references.isNotEmpty()) {
                    appendln()
                    appendln()
                    append(references.joinToString("\n") { it.toString() })
                }
            }
            .toString()
    }

    private fun StringBuilder.appendIndicesBlock() {
        append("  ")
        append("Indexes")
        append("  ")
        appendln("{")
        this@Table.indices.forEach { appendln(it.toString().prependIndent("    ")) }
        append("  ")
        appendln("}")
    }
}
