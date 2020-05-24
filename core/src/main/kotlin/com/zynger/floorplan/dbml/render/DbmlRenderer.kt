package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.Settings
import com.zynger.floorplan.dbml.Project

object DbmlRenderer {
    fun render(project: Project, settings: Settings): String {
        return project.tables.map { TableRenderer(it, settings).render() }.joinToString(separator = "\n\n")
    }
}