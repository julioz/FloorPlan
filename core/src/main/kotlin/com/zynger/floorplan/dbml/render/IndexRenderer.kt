package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.dbml.Index

class IndexRenderer(private val index: Index) {
    fun render(): String {
        return "Index ${index.name}"
    }
}