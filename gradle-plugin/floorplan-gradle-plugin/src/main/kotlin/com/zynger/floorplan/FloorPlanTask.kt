package com.zynger.floorplan

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File

open class FloorPlanTask : DefaultTask() {

    data class FloorPlanInput(
        val schemaLocation: String,
        val outputLocation: String
    )

    @get:InputDirectory
    lateinit var schemaLocation: File

    @OutputDirectory
    lateinit var outputLocation: File

    @TaskAction
    fun generateFloorPlan() {
        println("Hello FloorPlan gradle!")
    }
}
