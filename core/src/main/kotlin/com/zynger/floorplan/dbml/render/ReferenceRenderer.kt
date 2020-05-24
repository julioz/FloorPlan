package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.dbml.Reference
import java.lang.StringBuilder

class ReferenceRenderer(private val reference: Reference) {
    private val fromTable = reference.fromTable
    private val fromColumn = reference.fromColumn
    private val toTable = reference.fromTable
    private val toColumn = reference.toColumn
    private val deleteAction = reference.deleteAction
    private val updateAction = reference.updateAction

    fun render(): String {
        return StringBuilder("Ref: ")
            .append(fromTable)
            .append(".")
            .append(fromColumn)
            .append(" - ")
            .append(toTable)
            .append(".")
            .append(toColumn)
            .apply {
                if (deleteAction != null || updateAction != null) {
                    renderActions()
                }
            }
            .toString()
    }

    private fun StringBuilder.renderActions() {
        append(" [")
        if (deleteAction != null) {
            append("delete: ")
            append(deleteAction)
            if (updateAction != null) {
                append(", ")
            }
        }
        if (updateAction != null) {
            append("update: ")
            append(updateAction)
        }
        append("]")
    }
}
