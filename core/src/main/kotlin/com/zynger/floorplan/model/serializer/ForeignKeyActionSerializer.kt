package com.zynger.floorplan.model.serializer

import com.zynger.floorplan.model.ForeignKeyAction
import kotlinx.serialization.*

@Serializer(forClass = ForeignKeyAction::class)
object ForeignKeyActionSerializer {

    override val descriptor: SerialDescriptor
        get() = PrimitiveDescriptor("foreignKeyActionSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ForeignKeyAction {
        val value = decoder.decodeString().toUpperCase()
        return ForeignKeyAction.values().find { it.key == value }!!
    }

    override fun serialize(encoder: Encoder, value: ForeignKeyAction) = encoder.encodeString(value.key)
}
