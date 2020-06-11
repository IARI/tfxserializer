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
    abstract fun convertStoT(oldT: T, oldS: S, s: S): T
    abstract fun convertTtoS(oldS: S, oldT: T, t: T): S
    val listenerS: ChangeListener<S> by lazy {
        createChangeListenerSetting(propertyT, ::convertStoT)
    }
    val listenerT: ChangeListener<T> by lazy {
        createChangeListenerSetting(propertyS, ::convertTtoS)
    }

    open fun bind() {
        // change propertyT based on value from propertyS
        triggerUpdate()
        propertyS.addListener(listenerS)
        propertyT.addListener(listenerT)
    }

    fun triggerUpdate() {
        // change propertyT based on value from propertyS
        listenerS.changed(propertyS, propertyS.value, propertyS.value)
    }

    protected fun <T1, T2> createChangeListenerSetting(
        changeProp: Property<T2>,
        converter: (T2, T1, T1) -> T2
    ): ChangeListener<T1> = object : ChangeListener<T1> {
        override fun changed(observable: ObservableValue<out T1>?, oldValue: T1, newValue: T1) {
            updating.guarded {
                val oldT2 = changeProp.value
                changeProp.value = converter.invoke(oldT2, oldValue, newValue)
            }
        }
    }

    open fun unbind() {
        propertyS.removeListener(listenerS)
        propertyT.removeListener(listenerT)
    }
}