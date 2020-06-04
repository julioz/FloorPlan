package com.zynger.floorplan

import com.zynger.floorplan.dbml.Reference
import com.zynger.floorplan.dbml.ReferenceOrder
import org.gradle.api.Plugin
import org.gradle.api.Project

class FloorPlanPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.create("codeLines") {
            it.doLast {
                val ref = Reference("fromT", "fromC", "toT", "toC", ReferenceOrder.ManyToOne)
                println("Hello from plugin! Ref is $ref")
            }
        }
    }
}
