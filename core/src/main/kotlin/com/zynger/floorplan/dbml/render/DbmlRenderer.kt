package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.Settings
import com.zynger.floorplan.dbml.Project

object DbmlRenderer {
    fun render(project: Project, settings: Settings): String {
        return project.tables.map {
            val referencesFromTable = project.references.filter { reference -> reference.fromTable == it.name }
            TableRenderer(it, referencesFromTable, settings).render()
        }.joinToString(separator = "\n\n")
    }
}