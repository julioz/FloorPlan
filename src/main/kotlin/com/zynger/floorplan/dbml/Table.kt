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
            .append("\n")
            .apply {
                fields.forEach {
                    append("  ")
                    append(it)
                    append("\n")
                }
            }
            .append("\n")
            .apply {
                if (this@Table.indices.isNotEmpty()) {
                    append("  ")
                    append("Indexes")
                    append(" ")
                    append("{")
                    append("\n")
                    this@Table.indices.forEach {
                        append("    ")
                        append(it)
                        append("\n")
                    }
                    append("  ")
                    append("}")
                    append("\n")
                }
            }
            .append("}")
            .append("\n")
            .append("\n")
            .apply {
                references.forEach {
                    append(it)
                    append("\n")
                }
            }.toString()
    }
}
