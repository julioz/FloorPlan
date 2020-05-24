package com.zynger.floorplan.dbml

import java.lang.IllegalArgumentException

// Ref: trending_shows.show_id - shows.id [delete: cascade, update: cascade]
data class Reference(
    val fromTable: String,
    val fromColumn: String,
    val toTable: String,
    val toColumn: String,
    val referenceOrder: ReferenceOrder,
    val updateAction: String? = null,
    val deleteAction: String? = null,
    val rawValue: String = "$fromTable.$fromColumn $referenceOrder $toTable.$toColumn"
)

enum class ReferenceOrder {
    OneToOne, OneToMany, ManyToOne;

    override fun toString(): String {
        return when(this) {
            OneToOne -> "-"
            ManyToOne -> ">"
            OneToMany -> "<"
        }
    }
    companion object {
        fun fromString(str: String): ReferenceOrder {
            return when (str.trim()) {
                "-" -> OneToOne
                ">" -> ManyToOne
                "<" -> OneToMany
                else -> throw IllegalArgumentException("Could not parse $str as reference order.")
            }
        }
    }
}
