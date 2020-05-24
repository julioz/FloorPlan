package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.dbml.Reference

class ReferenceRenderer(private val reference: Reference) {
    fun render(): String {
        return "ref ${reference.fromTable}"
    }
}
