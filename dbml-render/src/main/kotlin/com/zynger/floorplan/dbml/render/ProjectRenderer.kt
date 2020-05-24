package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.Settings
import com.zynger.floorplan.dbml.Project

object ProjectRenderer {
    fun render(
        project: Project,
        settings: Settings
    ): String {
        return project.tables.joinToString(separator = "\n\n") { table ->
            TableRenderer(
                table = table,
                referencesFromTable = project.references.filter { reference -> reference.fromTable == table.name },
                settings = settings
            ).render()
        }
    }
}
