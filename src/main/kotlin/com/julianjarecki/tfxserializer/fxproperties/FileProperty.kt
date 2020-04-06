package com.julianjarecki.tfxserializer.fxproperties

import com.julianjarecki.tfxserializer.utils.userHomeDir
import com.julianjarecki.tfxserializer.utils.absolutePath
import com.julianjarecki.tfxserializer.utils.stringconverter
import com.julianjarecki.tfxserializer.utils.toFile
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.serialization.*
import java.io.File
import java.nio.file.Path

@Serializable
class FileProperty(@Transient private val f: File = userHomeDir) : SimpleObjectProperty<File>(f) {
    constructor(f: String) : this(f.toFile)
    constructor(f: Path) : this(f.toFile())

    @Transient
    val absolutePath = absolutePath()

    @Transient
    val name = SimpleStringProperty(f.name).apply {
        bindBidirectional(this@FileProperty, converter)
    }

    fun resolve(relPath: String) = value.toPath().resolve(relPath).toFile()

    @Serializer(forClass = FileProperty::class)
    companion object : KSerializer<FileProperty> {
        override fun deserialize(decoder: Decoder) = FileProperty(decoder.decodeString().toFile)

        override fun serialize(encoder: Encoder, value: FileProperty) {
            encoder.encodeString(value.value?.absolutePath ?: "NULL")
        }

        @Transient
        val converter = File::getAbsolutePath stringconverter String::toFile
    }
}