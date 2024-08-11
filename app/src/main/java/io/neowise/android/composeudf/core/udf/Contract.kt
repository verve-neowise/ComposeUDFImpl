package io.neowise.android.composeudf.core.udf

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


interface UDF {
    interface State
    interface Action {

        data class ShowMessage(val message: String) : Action
    }
    interface Event {
        data class ShowMessage(val message: String, val delay: Long = 0) : Event
    }
    interface Effect
}

interface ReducerScope {
    fun sendEvent(vararg event: UDF.Event)
    fun sendEffect(vararg effects: UDF.Effect)
}

interface EffectorScope {
    fun dispatch(action: UDF.Action)
    fun coroutine(block: suspend CoroutineScope.() -> Unit): Job
}
