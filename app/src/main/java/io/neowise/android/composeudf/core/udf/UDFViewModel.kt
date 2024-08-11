package io.neowise.android.composeudf.core.udf

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

abstract class UDFViewModel<S : UDF.State>(
    initialState: S,
    eventBufferSize: Int = EVENT_BUFFER_SIZE
) : ViewModel() {

    companion object {
        const val EVENT_BUFFER_SIZE = 3
    }

    private val _state: MutableState<S> = mutableStateOf(initialState)

    val state: State<S> = _state

    private val _events: MutableSharedFlow<UDF.Event> = MutableSharedFlow(
        extraBufferCapacity = eventBufferSize,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val events: EventsHolder<UDF.Event> = EventsHolder(
        events = _events.asSharedFlow()
    )

    private val actionChannel = Channel<UDF.Action>(3)

    private val effectorScope = object : EffectorScope {
        override fun dispatch(action: UDF.Action) {
            actionChannel.trySend(action)
        }

        override fun coroutine(block: suspend CoroutineScope.() -> Unit): Job {
            return viewModelScope.launch(block = block)
        }
    }

    private val reducerScope = object : ReducerScope {

        override fun sendEvent(vararg event: UDF.Event) {
            event.forEach { _events.tryEmit(it) }
        }

        override fun sendEffect(vararg effects: UDF.Effect) {
            proceedEffects(effects)
        }
    }

    private val delegates = mutableListOf<SliceDelegate<S>>()

    protected fun <ST: UDF.State> use(
        slice: Slice<ST>,
        stateProvider: (S) -> UDF.State,
        transform: (S, ST) -> S
    ) {
        viewModelScope.launch {
            slice.state.collect {
                _state.value = transform(_state.value, it)
            }
        }
        delegates.add(SliceDelegate(stateProvider, slice::dispatch, slice::affect))
    }

    protected abstract fun ReducerScope.reduce(action: UDF.Action, state: S): S
    protected abstract suspend fun EffectorScope.affect(effect: UDF.Effect)

    init {
        proceedActions()
    }

    val dispatch: (UDF.Action) -> Unit = { action: UDF.Action ->
        actionChannel.trySend(action)
    }

    private fun proceedActions() {
        viewModelScope.launch {
            while (isActive) {
                val action = actionChannel.receive()
                try {
                    val state = reducerScope.reduce(action, state.value)
                    _state.value = state
                }
                catch (_: NotDispatchedException) {
                    try {
                        proceedActionDelegates(action)
                    } catch (e: Exception) {
                        Log.e("UDFViewModel", "Action Not dispatched", e)
                        throw e
                    }
                }
            }
        }
    }

    private fun proceedActionDelegates(action: UDF.Action) {
        val dispatched = delegates.any { delegate ->
            try {
                delegate.dispatch(reducerScope, action, delegate.stateProvider(_state.value))
                true
            } catch (_: NotDispatchedException) {
                false
            }
        }

        if (!dispatched) {
            ActionNotDispatched(action)
        }
    }

    private fun proceedEffects(effects: Array<out UDF.Effect>) {
        effects.forEach { effect ->
            viewModelScope.launch {
                try {
                    effectorScope.affect(effect)
                }
                catch (e: NotDispatchedException) {
                    try {
                        proceedEffectDelegates(effect)
                    } catch (e: Exception) {
                        throw e
                    }
                }
            }
        }
    }

    private suspend fun proceedEffectDelegates(effect: UDF.Effect) {
        val dispatched = delegates.any { delegate ->
            try {
                delegate.affect(effectorScope, effect)
                true
            } catch (_: NotDispatchedException) {
                false
            }
        }
        if (!dispatched) {
            EffectNotDispatched(effect)
        }
    }

    override fun onCleared() {
        super.onCleared()
        actionChannel.close()
    }
}