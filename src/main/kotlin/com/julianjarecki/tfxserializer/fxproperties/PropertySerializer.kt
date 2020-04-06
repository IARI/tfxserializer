package com.julianjarecki.tfxserializer.app.fxproperties

import javafx.beans.property.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
import java.io.File

@Serializer(forClass = SimpleStringProperty::class)
open class StringPropertySerializer : KSerializer<SimpleStringProperty> {
    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = SimpleStringProperty(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: SimpleStringProperty) {
        encoder.encodeString(value.value ?: "NULL")
    }
}

@Serializer(forClass = SimpleIntegerProperty::class)
open class IntPropertySerializer : KSerializer<SimpleIntegerProperty> {
    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder) = SimpleIntegerProperty(decoder.decodeInt())

    override fun serialize(encoder: Encoder, value: SimpleIntegerProperty) {
        encoder.encodeInt(value.value ?: 0)
    }
}

@Serializer(forClass = SimpleBooleanProperty::class)
open class BoolPropertySerializer : KSerializer<SimpleBooleanProperty> {
    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.BOOLEAN)

    override fun deserialize(decoder: Decoder) = SimpleBooleanProperty(decoder.decodeBoolean())

    override fun serialize(encoder: Encoder, value: SimpleBooleanProperty) {
        encoder.encodeBoolean(value.value ?: false)
    }
}

@Serializer(forClass = SimpleDoubleProperty::class)
open class DoublePropertySerializer : KSerializer<SimpleDoubleProperty> {
    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder) = SimpleDoubleProperty(decoder.decodeDouble())

    override fun serialize(encoder: Encoder, value: SimpleDoubleProperty) {
        encoder.encodeDouble(value.value ?: .0)
    }
}

@Serializer(forClass = SimpleFloatProperty::class)
open class FloatPropertySerializer : KSerializer<SimpleFloatProperty> {
    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.FLOAT)

    override fun deserialize(decoder: Decoder) = SimpleFloatProperty(decoder.decodeFloat())

    override fun serialize(encoder: Encoder, value: SimpleFloatProperty) {
        encoder.encodeFloat(value.value ?: 0f)
    }
}


val jfxModule = SerializersModule {
    contextual(SimpleStringProperty::class, StringPropertySerializer())
    contextual(SimpleBooleanProperty::class, BoolPropertySerializer())
    contextual(SimpleDoubleProperty::class, DoublePropertySerializer())
    contextual(SimpleIntegerProperty::class, IntPropertySerializer())
    contextual(SimpleFloatProperty::class, FloatPropertySerializer())
}


val jfxJsonSerializer by lazy {
    Json(JsonConfiguration.Stable, context = jfxModule)
}
val jfxJsonSerializerNonstrict by lazy {
    Json(
        JsonConfiguration.Stable.copy(
            ignoreUnknownKeys = true
            //isLenient = true
        ), context = jfxModule
    )
}

fun <T : Any> Property<T>.readFrom(text: String, deserializer: DeserializationStrategy<T>, strict: Boolean = false) {
    value = (if (strict) jfxJsonSerializer else jfxJsonSerializerNonstrict).parse(deserializer, text)
}

fun <T : Any> Property<T>.readFrom(file: File, deserializer: DeserializationStrategy<T>, strict: Boolean = false) =
    readFrom(file.readText(), deserializer, strict)
