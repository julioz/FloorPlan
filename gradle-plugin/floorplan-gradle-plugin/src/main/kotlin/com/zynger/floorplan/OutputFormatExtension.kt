package com.zynger.floorplan

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class OutputFormatExtension
@Inject
constructor(
    objects: ObjectFactory
) {

    val dbmlConfiguration: DbmlConfigurationExtension = objects.newInstance(DbmlConfigurationExtension::class.java)
    val svgConfiguration: SvgConfigurationExtension = objects.newInstance(SvgConfigurationExtension::class.java)
    val pngConfiguration: PngConfigurationExtension = objects.newInstance(PngConfigurationExtension::class.java)
    val dotConfiguration: DotConfigurationExtension = objects.newInstance(DotConfigurationExtension::class.java)

    fun dbml(action: Action<DbmlConfigurationExtension>) {
        action.execute(dbmlConfiguration)
    }
    fun svg(action: Action<OutputFormatConfiguration>) {
        action.execute(svgConfiguration)
    }
    fun png(action: Action<OutputFormatConfiguration>) {
        action.execute(pngConfiguration)
    }
    fun dot(action: Action<OutputFormatConfiguration>) {
        action.execute(dotConfiguration)
    }
}

sealed class OutputFormatConfiguration(objects: ObjectFactory) {
    val enabled: Property<Boolean> = objects.property(Boolean::class.java).also { it.convention(false) }

    fun enabled(value: Boolean) {
        enabled.set(value)
        enabled.disallowChanges()
    }
}

open class DbmlConfigurationExtension
@Inject
constructor(
    objects: ObjectFactory
): OutputFormatConfiguration(objects) {
    val creationSqlAsTableNote: Property<Boolean> = objects.property(Boolean::class.java).also { it.convention(false) }
    val renderNullableFields: Property<Boolean> = objects.property(Boolean::class.java).also { it.convention(false) }

    fun creationSqlAsTableNote(enabled: Boolean) {
        creationSqlAsTableNote.set(enabled)
        creationSqlAsTableNote.disallowChanges()
    }

    fun renderNullableFields(enabled: Boolean) {
        renderNullableFields.set(enabled)
        renderNullableFields.disallowChanges()
    }
}

open class SvgConfigurationExtension
@Inject
constructor(
    objects: ObjectFactory
):  OutputFormatConfiguration(objects)

open class PngConfigurationExtension
@Inject
constructor(
    objects: ObjectFactory
):  OutputFormatConfiguration(objects)

open class DotConfigurationExtension
@Inject
constructor(
    objects: ObjectFactory
):  OutputFormatConfiguration(objects)
