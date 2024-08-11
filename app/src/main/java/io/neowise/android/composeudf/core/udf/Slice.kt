package io.neowise.android.composeudf.core.udf

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class Slice<S: UDF.State>(initialValue: S) {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialValue)
    internal val state: StateFlow<S> = _state.asStateFlow()

    abstract fun ReducerScope.reduce(action: UDF.Action, state: S): S
    abstract suspend fun EffectorScope.affect(effect: UDF.Effect)

    internal fun dispatch(reducerScope: ReducerScope, action: UDF.Action, state: UDF.State) {
        @Suppress("UNCHECKED_CAST")
        if (state as? S != null) {
            _state.update { reducerScope.reduce(action, state) }
        }
    }

    internal suspend fun affect(effectorScope: EffectorScope, effect: UDF.Effect) {
        return effectorScope.affect(effect)
    }
}

internal class SliceDelegate<S>(
    val stateProvider: (S) -> UDF.State,
    val dispatch: (ReducerScope, UDF.Action, UDF.State) -> Unit,
    val affect: suspend (effectorScope: EffectorScope, effect: UDF.Effect) -> Unit
)