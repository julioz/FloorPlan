package com.zynger.floorplan.gradle.extension

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class FloorPlanExtension
@Inject
constructor(
    objects: ObjectFactory
) {
    val schemaLocation: Property<String> = objects.property(String::class.java)
    val outputLocation: Property<String> = objects.property(String::class.java)
}
