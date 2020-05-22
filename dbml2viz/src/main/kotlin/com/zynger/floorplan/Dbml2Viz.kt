package com.zynger.floorplan

import guru.nidi.graphviz.attribute.*
import guru.nidi.graphviz.attribute.Rank.RankDir
import guru.nidi.graphviz.attribute.Rank.dir
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory.*
import guru.nidi.graphviz.model.MutableGraph


fun main() {
    val src = sample()
    //    val src = File("samples/dbml/db-track-pol.dbml").readText()
    val project = Parser.parse(src)
    val tables = project.tables
    val references = project.reference

    val g: MutableGraph = mutGraph("example1").setDirected(true)

    with(g.graphAttrs()) {
        add(GraphAttr.pad(0.5))
        add(Rank.sep(2.0))
        add(MapAttributes<ForNode>().add("nodesep", 0.5))
        add(dir(RankDir.LEFT_TO_RIGHT))
    }

    with(g.nodeAttrs()) {
        add(Shape.PLAIN)
    }

    tables.forEach {
        val htmlTable = buildString {
            appendln("<table border=\"0\" cellborder=\"1\" cellspacing=\"0\">")
            appendln("<tr><td bgcolor=\"darkolivegreen1\"><b>${it.name}</b></td></tr>")

            it.columns.forEach { column ->
                append("<tr><td port=\"${column.name}\">")
                if (column.primaryKey) {
                    append("<b>")
                }
                append("${column.name}: <i>${column.type}</i>")
                if (column.primaryKey) {
                    append("</b>")
                }
                appendln("</td></tr>")
            }
            if (it.indexes.isNotEmpty()) {
                appendln("<tr><td bgcolor=\"azure3\"><i>Indices</i></td></tr>")
                it.indexes.forEach { index ->
                    append("<tr><td>")
                    append(index.name)
                    appendln("</td></tr>")
                }
            }
            append("</table>")
        }
        val node = mutNode(it.name).add(Label.html(htmlTable))
        g.add(node)
    }

    references.forEach {
        val link = between(port(it.fromColumn), node(it.toTable).port(it.toColumn))
            .with(Label.of(it.referenceOrder.label))
            .with(Style.DASHED)

        g.add(mutNode(it.fromTable).addLink(link))
    }
    println(Graphviz.fromGraph(g).width(200).render(Format.DOT).toString())
}

private val ReferenceOrder.label: String
    get() = when (this) {
        ReferenceOrder.OneToOne -> "1-1"
        ReferenceOrder.OneToMany -> "1-*"
        ReferenceOrder.ManyToOne -> "*-*"
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