# tfxserializer
a collection of kotlin serializers for javafx properties and utilities to use them with tornadofx

Simple primitive properties, such as 

* `SimpleStringProperty`, 
* `SimpleBooleanProperty`, 
* `SimpleDoubleProperty`, 
* `SimpleIntegerProperty`, 
* `SimpleFloatProperty`
are serialized automatically (use `@ContextualSerialization`).

Lists that are defined as an `ObservableList<T>` can be Serialized using the generic `ObservableListSerializer`, if there is a known serializer for `T`:

```kotlin
    @Serializable(with = ObservableListSerializer::class)
```

similarly, `ObjectProperty<T>` values can be Serialized using the generic `ObjectPropertySerializer`, if there is a known serializer for `T`:

```kotlin
    @Serializable(with = ObjectPropertySerializer::class)
    val units = SimpleObjectProperty<Units>(Units.Millimeter)
```

Additionally, there are some special predefined properties which bring their own serializers with them:

* `FileProperty` extends `SimpleObjectProperty<File>`
* `ColorProperty` extends `SimpleObjectProperty<Color>`
* `IntervalProperty` extends `SimpleObjectProperty<IntRange>`


*Example class*:
Consider the following class that contains some settings for a TornadoFX application:

```kotlin
@Serializable
class AppSettings {
    @ContextualSerialization
    val openDocumentAfterExport = SimpleBooleanProperty(true)

    @Serializable(with = ObservableListSerializer::class)
    val knownDocuments = observableListOf<LabelsDocument>()

    val documentFolder = FileProperty(userHomeDir)
}
```
