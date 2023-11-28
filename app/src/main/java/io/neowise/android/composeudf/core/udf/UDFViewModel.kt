package io.neowise.android.composeudf.core.udf

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

abstract class UDFViewModel<S: UDF.State, A: UDF.Action, EF: UDF.Effect, E: UDF.Event>(initialState: S, eventBufferSize: Int = EVENT_BUFFER_SIZE) : ViewModel()  {

    companion object {
        const val EVENT_BUFFER_SIZE = 3
    }

    private val _state: MutableState<S> = mutableStateOf(initialState)

    val state: State<S> = _state

    private val _events: MutableSharedFlow<E> = MutableSharedFlow(
        extraBufferCapacity = eventBufferSize,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val events: Flow<E> = _events.asSharedFlow()

    private val actionChannel = Channel<A>(3)

    protected abstract fun reduce(action: A, state: S) : Update<S, EF, E>
    protected abstract fun affect(effect: EF): Flow<EffectResult<A, E>>

    init {
        proceed()
    }

    fun dispatch(action: A) {
        actionChannel.trySend(action)
    }

    private fun proceed() {
        viewModelScope.launch {
            while (isActive) {
                val action = actionChannel.receive()

                val update = reduce(action, state.value)

                if (update.state != null && update.state != _state.value) {
                    _state.value = update.state
                }

                update.events.forEach { event ->
                    sendEvent(event)
                }
                effectRuntime(update.effects)
            }
        }
    }

    private fun effectRuntime(effects: List<EF>) {
        effects.forEach { effect ->
            viewModelScope.launch {
                affect(effect).collect {
                    it.action?.let { action ->
                        dispatch(action)
                    }
                    it.events.forEach { event ->
                        sendEvent(event)
                    }
                }
            }
        }
    }

    protected fun sendEvent(event: E) {
        _events.tryEmit(event)
    }

    override fun onCleared() {
        super.onCleared()
        actionChannel.close()
    }
}