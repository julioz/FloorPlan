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
    val svgConfiguration: OutputFormatConfiguration = objects.newInstance(OutputFormatConfiguration::class.java)
    val pngConfiguration: OutputFormatConfiguration = objects.newInstance(OutputFormatConfiguration::class.java)
    val dotConfiguration: OutputFormatConfiguration = objects.newInstance(OutputFormatConfiguration::class.java)

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

    override fun toString(): String {
        return "OutputFormatExtension(" +
                "dbmlConfiguration=$dbmlConfiguration, " +
                "svgConfiguration=$svgConfiguration, " +
                "pngConfiguration=$pngConfiguration, " +
                "dotConfiguration=$dotConfiguration" +
                ")"
    }
}

abstract class OutputFormatConfiguration
@Inject
constructor(
    objects: ObjectFactory
) {
    val enabled: Property<Boolean> = objects.property(Boolean::class.java).also { it.convention(false) }

    fun enabled(value: Boolean) {
        enabled.set(value)
        enabled.disallowChanges()
    }

    override fun toString(): String {
        return "OutputFormatConfiguration(enabled=${enabled.get()})"
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

    override fun toString(): String {
        return "DbmlConfigurationExtension(" +
                "enabled=${enabled.get()}, " +
                "creationSqlAsTableNote=${creationSqlAsTableNote.get()}, " +
                "renderNullableFields=${renderNullableFields.get()}" +
                ")"
    }
}
