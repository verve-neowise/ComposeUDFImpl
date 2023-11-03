package io.neowise.android.composeudf.core.udf

interface UDF {
    interface Action
    interface Event
    interface Effect
    interface State
    object NoneEvent : Event
}