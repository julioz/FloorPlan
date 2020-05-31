package com.zynger.floorplan

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class OutputFormatExtension
@Inject
constructor(
    objects: ObjectFactory
) {
    var dbml: String? = null
    var svg: Property<String> = objects.property(String::class.java)
    var png: String? = null
    var dot: String? = null
}
