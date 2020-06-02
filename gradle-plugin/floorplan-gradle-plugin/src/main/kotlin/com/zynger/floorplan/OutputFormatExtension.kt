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

    val dbmlConfiguration = objects.newInstance(DbmlConfigurationExtension::class.java)

    fun dbml(action: Action<DbmlConfigurationExtension>) {
        action.execute(dbmlConfiguration)
    }

    var svg: Property<String> = objects.property(String::class.java)
    var png: String? = null
    var dot: String? = null
}

abstract class OutputFormatConfiguration(objects: ObjectFactory) {
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
