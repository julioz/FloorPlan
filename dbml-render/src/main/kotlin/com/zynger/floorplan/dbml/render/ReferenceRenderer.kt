package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.dbml.Reference
import java.lang.StringBuilder

class ReferenceRenderer(reference: Reference) {
    private val fromTable = reference.fromTable
    private val fromColumn = reference.fromColumn
    private val toTable = reference.toTable
    private val toColumn = reference.toColumn
    private val referenceOrder = reference.referenceOrder
    private val deleteAction = reference.deleteAction
    private val updateAction = reference.updateAction

    fun render(): String {
        return StringBuilder("Ref: ")
            .append(fromTable)
            .append(".")
            .append(fromColumn)
            .append(" $referenceOrder ")
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
