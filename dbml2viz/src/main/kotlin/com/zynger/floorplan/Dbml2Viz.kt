package com.zynger.floorplan

import java.io.File

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
    val table2 = Table(
        "TrackPolicies",
        listOf(
            Column("id", "int", primaryKey = true),
            Column("urn", "varchar"),
            Column("monetizable", "int"),
            Column("blocked", "int"),
            Column("snipped", "int"),
            Column("syncable", "int"),
            Column("sub_mid_tier", "int"),
            Column("sub_high_tier", "int"),
            Column("policy", "varchar"),
            Column("monetization_model", "varchar"),
            Column("last_updated", "int")
        ),
        listOf(
            Index("index_TrackPolicies_urn", listOf("urn"), unique = true)
        )
    )
    val tables = listOf(table1, table2)

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
