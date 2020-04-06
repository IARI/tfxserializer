package com.julianjarecki.tfxserializer.fxproperties

import com.julianjarecki.tfxserializer.utils.html
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color
import kotlinx.serialization.*

@Serializable
class ColorProperty(@Transient private val r: Color = Color.BLACK) : SimpleObjectProperty<Color>(r) {
    @Serializer(forClass = ColorProperty::class)
    companion object {
        override fun deserialize(decoder: Decoder): ColorProperty {
            return ColorProperty(Color.web(decoder.decodeString()))
        }

        override fun serialize(encoder: Encoder, value: ColorProperty) {
            (value.value ?: Color.BLACK).let {
                encoder.encodeString(it.html)
            }
        }
    }
}