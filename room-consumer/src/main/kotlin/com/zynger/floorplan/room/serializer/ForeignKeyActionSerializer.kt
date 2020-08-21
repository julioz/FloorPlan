package com.zynger.floorplan.room.serializer

import com.zynger.floorplan.room.ForeignKeyAction
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ForeignKeyActionSerializer: KSerializer<ForeignKeyAction> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("foreignKeyActionSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ForeignKeyAction {
        val value = decoder.decodeString().toUpperCase()
        return ForeignKeyAction.values().find { it.key == value }!!
    }

    override fun serialize(encoder: Encoder, value: ForeignKeyAction) = encoder.encodeString(value.key)
}
