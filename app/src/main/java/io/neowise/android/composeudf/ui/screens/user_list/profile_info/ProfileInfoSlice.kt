package io.neowise.android.composeudf.ui.screens.user_list.profile_info

import io.neowise.android.composeudf.core.udf.ActionNotDispatched
import io.neowise.android.composeudf.core.udf.EffectNotDispatched
import io.neowise.android.composeudf.core.udf.EffectorScope
import io.neowise.android.composeudf.core.udf.ReducerScope
import io.neowise.android.composeudf.core.udf.Slice
import io.neowise.android.composeudf.core.udf.UDF
import io.neowise.android.composeudf.ui.screens.user_list.profile_info.ProfileInfoContract.Action
import io.neowise.android.composeudf.ui.screens.user_list.profile_info.ProfileInfoContract.State

class ProfileInfoSlice : Slice<State>(State()) {
    override fun ReducerScope.reduce(action: UDF.Action, state: State) = when (action) {
        is Action.ToggleEdit -> {
            state.copy(isEdit = !state.isEdit)
        }

        is Action.NameChanged -> {
            state.copy(name = action.value)
        }

        is Action.EmailChanged -> {
            state.copy(email = action.value)
        }

        else -> {
            ActionNotDispatched(action)
        }
    }

    override suspend fun EffectorScope.affect(effect: UDF.Effect) {
        EffectNotDispatched(effect)
    }
}
