package com.zynger.floorplan

// Ref: trending_shows.show_id - shows.id [delete: cascade, update: cascade]
data class Reference(
    val fromTable: String,
    val fromColumn: String,
    val toTable: String,
    val toColumn: String,
    val referenceOrder: ReferenceOrder = ReferenceOrder.OneToOne // TODO adjust later to parse ref. order
)

enum class ReferenceOrder {
    // > many-to-one; < one-to-many; - one-to-one
    OneToOne, OneToMany, ManyToOne
}
