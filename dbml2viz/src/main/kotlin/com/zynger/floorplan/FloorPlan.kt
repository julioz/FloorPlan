package com.zynger.floorplan

import com.zynger.floorplan.Format.*
import com.zynger.floorplan.dbml.Project
import com.zynger.floorplan.dbml.Reference
import com.zynger.floorplan.dbml.ReferenceOrder
import com.zynger.floorplan.dbml.Table
import com.zynger.floorplan.dbml.render.ProjectRenderer
import guru.nidi.graphviz.attribute.*
import guru.nidi.graphviz.attribute.Attributes.attr
import guru.nidi.graphviz.attribute.Attributes.attrs
import guru.nidi.graphviz.attribute.Rank.RankDir
import guru.nidi.graphviz.attribute.Rank.dir
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory.*
import guru.nidi.graphviz.model.Link
import guru.nidi.graphviz.model.MutableGraph
import java.util.*

object FloorPlan {

    fun render(
        project: Project,
        output: Output
    ) {
        if (output.format is DBML) {
            val config = output.format.config
            val settings = Settings(config.creationSqlAsTableNote, config.renderNullableFields)
            val dbml = ProjectRenderer.render(project, settings)

            when (output.destination) {
                Destination.StandardOut -> println(dbml)
                is Destination.Disk -> {
                    output.destination.file.parentFile.mkdirs()
                    output.destination.file.writeText(dbml)
                }
            }
        } else {
            val graphviz = graph(project, output.notation)
            val renderer = when (output.format) {
                is DBML -> throw IllegalStateException()
                DOT -> graphviz.render(Format.DOT)
                SVG -> graphviz.render(Format.SVG)
                PNG -> graphviz.render(Format.PNG)
            }

            when (output.destination) {
                Destination.StandardOut -> println(renderer.toString())
                is Destination.Disk -> renderer.toFile(output.destination.file)
            }
        }
    }

    private fun graph(
        project: Project,
        notation: Notation
    ): Graphviz {
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
                appendLine("<table border=\"0\" cellborder=\"1\" cellspacing=\"0\">")
                appendLine("<tr><td bgcolor=\"darkolivegreen1\"><b>${it.name}</b></td></tr>")

                it.columns.forEach { column ->
                    append("<tr><td port=\"${column.name}\">")
                    if (column.primaryKey) {
                        append("<b>")
                    }
                    append("${column.name}: <i>${column.type}</i>")
                    if (column.primaryKey) {
                        append("</b>")
                    }
                    appendLine("</td></tr>")
                }
                if (it.indexes.isNotEmpty()) {
                    appendLine("<tr><td bgcolor=\"azure3\"><i>Indices</i></td></tr>")
                    it.indexes.forEach { index ->
                        append("<tr><td>")
                        append(index.name)
                        appendLine("</td></tr>")
                    }
                }
                append("</table>")
            }
            val node = mutNode(it.name)
                .add(Label.html(htmlTable))
                .add("tooltip", getTooltipForTable(it))
            g.add(node)
        }

        references.forEach {
            val link = when (notation) {
                Notation.Chen -> chenLink(it)
                Notation.CrowsFoot -> crowsFootLink(it)
            }

            g.add(mutNode(it.fromTable).addLink(link))
        }

        return Graphviz.fromGraph(g)
    }

    private fun chenLink(reference: Reference): Link {
        return between(port(reference.fromColumn), node(reference.toTable).port(reference.toColumn))
            .with(Label.of(reference.referenceOrder.label))
            .with(Style.DASHED)
    }

    private fun crowsFootLink(reference: Reference): Link {
        /**
         * Three symbols are used to represent cardinality:
         *  - the ring represents "zero" (dot)
         *  - the dash represents "one" (tee)
         *  - the crow's foot represents "many" or "infinite" (crow)
         *
         *  These symbols are used in pairs to represent the four types
         *  of cardinality that an entity may have in a relationship.
         *  The inner component of the notation represents the minimum,
         *  and the outer component represents the maximum.
         *
         *  - ring and dash → minimum zero, maximum one (optional)
         *  - dash and dash → minimum one, maximum one (mandatory)
         *  - ring and crow's foot → minimum zero, maximum many (optional)
         *  - dash and crow's foot → minimum one, maximum many (mandatory)
         */
        val teeTee = Arrow.TEE.and(Arrow.TEE)
        val crow = Arrow.CROW
        val (arrowtail, arrowhead) = when (reference.referenceOrder) {
            ReferenceOrder.OneToOne -> teeTee to teeTee
            ReferenceOrder.OneToMany -> teeTee to crow
            ReferenceOrder.ManyToOne -> crow to teeTee
        }

        val attributes: Attributes<ForLink> = attrs(
            attr("arrowhead", arrowhead.value),
            attr("arrowtail", arrowtail.value),
            attr("dir", Arrow.DirType.BOTH.name.toLowerCase(Locale.ENGLISH))
        )
        return between(port(reference.fromColumn), node(reference.toTable).port(reference.toColumn))
            .with(attributes)
    }

    private fun getTooltipForTable(table: Table): String {
        return buildString {
            append("Table ${table.name}")
            apply {
                if (table.note != null) {
                    appendLine()
                    append("Note: ${table.note}")
                }
            }
            apply {
                if (table.alias != null) {
                    appendLine()
                    append("Alias: ${table.alias}")
                }
            }
        }
    }

    private val ReferenceOrder.label: String
        get() = when (this) {
            ReferenceOrder.OneToOne -> "1-1"
            ReferenceOrder.OneToMany -> "1-*"
            ReferenceOrder.ManyToOne -> "*-1"
        }
}
