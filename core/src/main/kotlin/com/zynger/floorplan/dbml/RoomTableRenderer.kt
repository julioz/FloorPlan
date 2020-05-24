package com.zynger.floorplan.dbml

import com.zynger.floorplan.Settings
import com.zynger.floorplan.room.Entity

class RoomTableRenderer(
    entity: Entity,
    private val settings: Settings
) {
    private val tableName: String = entity.tableName
    private val roomFieldRenderers: List<RoomFieldRenderer> = entity.fields.map { RoomFieldRenderer(it, entity.primaryKey, settings) }
    private val roomIndexRenderers: List<RoomIndexRenderer> = entity.indices.map { RoomIndexRenderer(it) }
    private val roomReferenceRenderers: List<RoomReferenceRenderer> = entity.foreignKeys.map { RoomReferenceRenderer(tableName, it) }
    private val createSql: String = entity.createSql

    override fun toString(): String {
        return StringBuilder()
            .append("Table $tableName")
            .append(" ")
            .append("{")
            .appendln()
            .apply {
                append(roomFieldRenderers.joinToString("\n") { it.toString().prependIndent("  ") })
            }
            .appendln()
            .apply {
                if (this@RoomTableRenderer.roomIndexRenderers.isNotEmpty()) {
                    appendln()
                    appendIndicesBlock()
                }
            }
            .apply {
                if (settings.creationSqlAsTableNote) {
                    appendln("  ")
                    append("Note: ".prependIndent("  "))
                    append("'")
                    append(createSql)
                    append("'")
                    appendln()
                }
            }
            .append("}")
            .apply {
                if (this@RoomTableRenderer.roomReferenceRenderers.isNotEmpty()) {
                    appendln()
                    appendln()
                    append(roomReferenceRenderers.joinToString("\n") { it.toString() })
                }
            }
            .toString()
    }

    private fun StringBuilder.appendIndicesBlock() {
        append("  ")
        append("Indexes")
        append("  ")
        appendln("{")
        this@RoomTableRenderer.roomIndexRenderers.forEach { appendln(it.toString().prependIndent("    ")) }
        append("  ")
        appendln("}")
    }
}
