package com.zynger.floorplan

import com.zynger.floorplan.Format.*
import com.zynger.floorplan.dbml.Project
import com.zynger.floorplan.dbml.ReferenceOrder
import guru.nidi.graphviz.attribute.*
import guru.nidi.graphviz.attribute.Rank.RankDir
import guru.nidi.graphviz.attribute.Rank.dir
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory.*
import guru.nidi.graphviz.model.MutableGraph

object Dbml2Viz {

    fun render(
        project: Project,
        output: Output
    ) {
        val tables = project.tables
        val references = project.references

        val g: MutableGraph = mutGraph().setDirected(true) // TODO use actual project schema name if given

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

        val graphviz = Graphviz.fromGraph(g)
        val renderer = when (output.format) {
            DBML -> TODO("Not yet implemented. Waiting for model unification in https://github.com/julioz/FloorPlan/issues/25")
            DOT -> graphviz.render(Format.DOT)
            SVG -> graphviz.render(Format.SVG)
            PNG -> graphviz.render(Format.PNG)
        }

        when (output.destination) {
            Destination.StandardOut -> println(renderer.toString())
            is Destination.Disk -> renderer.toFile(output.destination.file)
        }
    }

    private val ReferenceOrder.label: String
        get() = when (this) {
            ReferenceOrder.OneToOne -> "1-1"
            ReferenceOrder.OneToMany -> "1-*"
            ReferenceOrder.ManyToOne -> "*-1"
        }
}
