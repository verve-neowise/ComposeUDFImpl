package io.neowise.android.composeudf.core.udf

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.State as ComposeState

abstract class UDFViewModel<A : UDF.Action, EF: UDF.Effect, S: UDF.State>(initialState: S) : ViewModel()  {

    private val _state: MutableState<S> = mutableStateOf(initialState)
    val state: ComposeState<S> = _state

    private val _events: MutableSharedFlow<UDF.Event> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: Flow<UDF.Event> = _events

    protected abstract fun reduce(action: A, state: S) : Update<S, EF>
    protected abstract fun effect(effect: EF): Flow<A>

    fun action(action: A) {
        val (newState, effects) = reduce(action, state.value)
        _state.value = newState
        effectRuntime(effects, ::action)
    }

    private fun effectRuntime(effects: Set<EF>, reducer: (A) -> Unit) {
        effects.forEach { effect ->
            viewModelScope.launch {
                effect(effect).collect {
                    reducer(it)
                }
            }
        }
    }

    protected fun event(event: UDF.Event) {
        _events.tryEmit(event)
    }
}