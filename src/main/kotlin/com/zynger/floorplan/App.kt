package com.zynger.floorplan

import com.zynger.floorplan.model.*
import java.io.File
import kotlinx.serialization.json.*
import java.lang.IllegalArgumentException
import java.lang.StringBuilder

fun main() {
    val src = File("samples/db.json")

    val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true, isLenient = true))
    val schema: Schema = json.parse(Schema.serializer(), src.readText())

    val entities = schema.database.entities

    /*
    Table merchants {
  id int [pk]
  merchant_name varchar
  country_code int
  "created at" varchar
  admin_id int [ref: > U.id] // inline relationship (many-to-one)
}

     */

    entities.forEach { entity ->
        val sb = StringBuilder()
            .append("Table ${entity.tableName}")
            .append(" ")
            .append("{")
            .append("\n")
            .apply {
                entity.fields.map { field -> renderField(field, entity.primaryKey) }.forEach { renderedField ->
                    append("  $renderedField")
                    append("\n")
                }
            }
            .append("\n")
            .apply {
                if (entity.indices.isNotEmpty()) {
                    append("  ")
                    append("Indexes")
                    append(" ")
                    append("{")
                    append("\n")
                    entity.indices.map { index -> renderIndex(index) }.forEach { renderedIndex ->
                        append("    $renderedIndex")
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
                entity.foreignKeys.map { foreignKey -> renderReference(entity.tableName, foreignKey) }.forEach { reference ->
                    append(reference)
                    append("\n")
                }
            }
        println(sb.toString())
    }
}

fun renderIndex(index: Index): String {
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

fun renderReference(fromTable: String, foreignKey: ForeignKey): String {
    // Ref: U.country_code > countries.code
    // > many-to-one; < one-to-many; - one-to-one

    val fromColumn = foreignKey.columns.first()
    val toTable = foreignKey.table
    val toColumn = foreignKey.referencedColumns.first()
    return StringBuilder("Ref: ")
        .append(fromTable)
        .append(".")
        .append(fromColumn)
        .append(" - ")
        .append(toTable)
        .append(".")
        .append(toColumn)
        .toString()
}

fun renderField(field: Field, primaryKey: PrimaryKey): String {
    val type = field.affinity.toType()
    val isPrimaryKey = field.isPrimaryKey(primaryKey)

    return StringBuilder(field.columnName)
        .append(" ")
        .append(type)
        .apply {
            append(" ")
            append("[")
            if (isPrimaryKey) {
                append("pk")
                if (primaryKey.autoGenerate) {
                    append(", increment")
                }
                if (field.notNull) {
                    append(", not null")
                }
            } else {
                append("note: ")
                append(if (field.notNull) "'not null'" else "'nullable'")
            }
            append("]")
        }
        .toString()
}

private fun Field.isPrimaryKey(primaryKey: PrimaryKey): Boolean {
    return primaryKey.columnNames.any { it == this.columnName }
}

private fun String.toType(): String {
    return when (this) {
        "TEXT" -> "varchar"
        "INTEGER" -> "int"
        else -> throw IllegalArgumentException("Unrecognized affinity $this")

    }
}
