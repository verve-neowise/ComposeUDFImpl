package io.neowise.android.composeudf.ui.screens.user_list

import androidx.compose.ui.text.input.TextFieldValue
import io.neowise.android.composeudf.core.udf.ActionNotDispatched
import io.neowise.android.composeudf.core.udf.EffectNotDispatched
import io.neowise.android.composeudf.core.udf.EffectorScope
import io.neowise.android.composeudf.core.udf.ReducerScope
import io.neowise.android.composeudf.core.udf.UDF
import io.neowise.android.composeudf.core.udf.UDFViewModel
import io.neowise.android.composeudf.data.UserRepositoryImpl
import io.neowise.android.composeudf.domain.UserRepository
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.Action
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.Effect
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.Event
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.State
import io.neowise.android.composeudf.ui.screens.user_list.profile_info.ProfileInfoSlice

class UserListViewModel : UDFViewModel<State>(State()) {

    private val userRepository: UserRepository = UserRepositoryImpl()
    private val profileInfoSlice = ProfileInfoSlice()

    init {
        use(profileInfoSlice, { state -> state.profileInfoState }) { state, profileInfoState ->
            state.copy(profileInfoState = profileInfoState)
        }
    }

    override fun ReducerScope.reduce(action: UDF.Action, state: State): State = when(action) {
        is Action.Load -> {
            sendEffect(Effect.FetchUsers)
            state.copy(isLoading = true)
        }
        is Action.CreateUser -> {
            val name = state.name.text.trim()
            val surname = state.surname.text.trim()

            if (name.isEmpty() || surname.isEmpty()) {
                sendEvent(Event.ShowError("Fill entries"))
                state
            }
            else {
                sendEffect(Effect.PostUser(state.name.text, state.surname.text))
                state.copy(
                    name = TextFieldValue(),
                    surname = TextFieldValue(),
                    isAddEnabled = false
                )
            }
        }
        is Action.UserListChanged -> {
            state.copy(isLoading = false, users = action.users)
        }
        is Action.UserAppended -> {
            state.copy(isAddEnabled = true, users = state.users + action.user)
        }
        is Action.NameChanged -> {
            state.copy(name = action.value)
        }
        is Action.SurnameChanged -> {
            state.copy(surname = action.value)
        }
        is Action.TriggerFakeEvent -> {
            sendEvent(Event.FakeNavigate)
            state
        }
        else -> {
            ActionNotDispatched(action)
        }
    }

    override suspend fun EffectorScope.affect(effect: UDF.Effect) {
        when (effect) {
            Effect.FetchUsers -> {
                userRepository.getUsers().collect {
                    dispatch(Action.UserListChanged(it))
                }
            }
            is Effect.PostUser -> {
                userRepository.createUser(effect.name, effect.username).collect {
                    dispatch(Action.UserAppended(it))
                }
            }
            else -> {
                EffectNotDispatched(effect)
            }
        }
    }
}