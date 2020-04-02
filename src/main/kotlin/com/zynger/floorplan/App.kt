package com.zynger.floorplan

import com.zynger.floorplan.model.Field
import com.zynger.floorplan.model.Schema
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
                entity.fields.map { field -> renderField(field) }.forEach { renderedField ->
                    append("  $renderedField")
                    append("\n")
                }
            }
            .append("\n")
            .append("}")
            .append("\n")
        println(sb.toString())
    }
}

fun renderField(field: Field): String {
    val type = field.affinity.toType()
    return "${field.columnName} $type"
}

private fun String.toType(): String {
    return when (this) {
        "TEXT" -> "varchar"
        "INTEGER" -> "int"
        else -> throw IllegalArgumentException("Unrecognized affinity $this")

    }
}
