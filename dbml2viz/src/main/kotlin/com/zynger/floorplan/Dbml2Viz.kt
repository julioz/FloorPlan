package com.zynger.floorplan

import java.io.File

fun main() {
    val src = sample()
//    val src = File("samples/dbml/db-track-pol.dbml").readText()
    val project = Parser.parse(src)
    val tables = project.tables
    val references = project.reference

    // TODO write to .gv file instead of stdout

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
    references.forEach { println(it.render()) }

    println("}")
}

private fun Reference.render(): String {
    // Foo:2 -> Baz:a [taillabel="a to b" labeltooltip="this is a tooltip"];
    return "$fromTable:$fromColumn -> $toTable:$toColumn [label=\"${referenceOrder.label}\"]"
}

private val ReferenceOrder.label: String
    get() = when (this) {
        ReferenceOrder.OneToOne -> "1-1"
        ReferenceOrder.OneToMany -> "1-*"
        ReferenceOrder.ManyToOne -> "*-*"
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

fun sample(): String {
    return """
Table posts {
  id int [pk, increment]
  title varchar [not null]
}

table comments {
  id int [pk,increment]
  comment varchar 
  post_id int [not null,ref: > posts.id]
}
table tags as aliasForTagsTable{
  id int [pk, increment, not null]
  title varchar [not null]
}

table post_tags [note: 'hey table note']{
  id int [pk]
  post_id int
  tag_id int
}




Ref: "tags"."id" < "post_tags"."tag_id"

Ref: "posts"."id" < "post_tags"."post_id"
Ref: "posts"."id" - "post_tags"."post_id"
    """.trimIndent()
}