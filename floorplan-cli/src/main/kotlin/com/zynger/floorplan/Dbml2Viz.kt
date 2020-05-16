package com.zynger.floorplan

import java.io.File

data class Index(val name: String, val columnNames: List<String>? = null, val unique: Boolean = false){
    // TODO should we enforce column names?
    //(urn) [name:'index_TimeToLives_urn', unique]
}
data class Column(val name: String, val type: String, val note: String? = null, val primaryKey: Boolean = false) {
    // address varchar(255) [unique, not null, note: 'to include unit number']
}
data class Table(val name: String, val columns: List<Column>, val indexes: List<Index> = emptyList())

fun main() {
//    val src = File("samples/dbml/single-table.dbml")
//    src.readLines().forEach { println(it) }

    val table1 = Table(
        "TimeToLives",
        listOf(
            Column("urn", "varchar"),
            Column("expireAt", "int"),
            Column("id", "int", primaryKey = true)
        ),
        listOf(
            Index("index_TimeToLives_urn", listOf("urn"), unique = true)
        )
    )
    val tables = listOf(table1)

    println(
        """
        digraph {
        graph [pad="0.5", nodesep="0.5", ranksep="2"];
        node [shape=plain];
        rankdir=LR;
        """.trimIndent()
    )
    println()

    tables.forEach { println(it.render()) }

    println()
    println("}")
}

private fun Table.render(): String {
    return buildString {
        appendln("$name [label=<")
        appendln("<table border=\"0\" cellborder=\"1\" cellspacing=\"0\">")
        appendln("<tr><td bgcolor=\"darkolivegreen1\"><b>$name</b></td></tr>")
        columns.forEach {
            append("<tr><td port=\"${it.name}\">")
            if (it.primaryKey) {
                append("<b>")
            }
            append("${it.name}: <i>${it.type}</i>")
            if (it.primaryKey) {
                append("</b>")
            }
            appendln("</td></tr>")
        }
        if (!indexes.isEmpty()) {
            appendln("<tr><td bgcolor=\"azure3\"><i>Indices</i></td></tr>")
            indexes.forEach { index ->
                append("<tr><td>")
                append(index.name)
                appendln("</td></tr>")
            }
        }
        append("</table>>];")
    }
}
