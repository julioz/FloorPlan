package com.zynger.floorplan.room

import com.zynger.floorplan.room.serializer.ForeignKeyActionSerializer
import kotlinx.serialization.Serializable

@Serializable
data class ForeignKey (
    val table: String,
    val columns: List<String>,
    val referencedColumns: List<String>,
    val onDelete: ForeignKeyAction,
    val onUpdate: ForeignKeyAction
)

/**
 * https://www.sqlite.org/foreignkeys.html#fk_actions
 */
@Serializable(with = ForeignKeyActionSerializer::class)
enum class ForeignKeyAction(val key: String) {
    NO_ACTION("NO ACTION"),
    RESTRICT("RESTRICT"),
    SET_NULL("SET NULL"),
    SET_DEFAULT("SET DEFAULT"),
    CASCADE("CASCADE")
}
