package com.julianjarecki.tfxserializer.utils

import javafx.beans.property.Property
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue

abstract class BidirectionalConversionBinding<S, T> protected constructor(
    protected val propertyS: Property<S>,
    protected val propertyT: Property<T>
) {
    init {
        if (propertyT.isBound) throw IllegalArgumentException("Bound property is not allowed.")
    }

    val updating = SimpleBooleanProperty()
    abstract fun convertStoT(s: S): T
    abstract fun convertTtoS(t: T): S
    val listenerS: ChangeListener<S> by lazy {
        createChangeListenerSetting(propertyT, ::convertStoT)
    }
    val listenerT: ChangeListener<T> by lazy {
        createChangeListenerSetting(propertyS, ::convertTtoS)
    }

    open fun bind() {
        propertyT.value = convertStoT(propertyS.value)
        propertyS.addListener(listenerS)
        propertyT.addListener(listenerT)
    }

    fun triggerUpdate() {
        updating.guarded {
            propertyT.value = convertStoT(propertyS.value)
        }
    }

    protected fun <T1, T2> createChangeListenerSetting(
        changeProp: Property<T2>,
        converter: (T1) -> T2
    ): ChangeListener<T1> =
        ChangeListener { _: ObservableValue<out T1>?, _: T1, new: T1 ->
            updating.guarded {
                changeProp.value = converter.invoke(new)
            }
        }

    open fun unbind() {
        propertyS.removeListener(listenerS)
        propertyT.removeListener(listenerT)
    }
}