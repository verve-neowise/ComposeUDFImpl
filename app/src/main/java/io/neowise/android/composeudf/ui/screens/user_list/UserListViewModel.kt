package io.neowise.android.composeudf.ui.screens.user_list

import androidx.compose.ui.text.input.TextFieldValue
import io.neowise.android.composeudf.core.udf.EffectResult
import io.neowise.android.composeudf.core.udf.UDFViewModel
import io.neowise.android.composeudf.core.udf.Update
import io.neowise.android.composeudf.core.udf.asResult
import io.neowise.android.composeudf.data.UserRepositoryImpl
import io.neowise.android.composeudf.domain.UserRepository
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.Action
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.Effect
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.Event
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserListViewModel : UDFViewModel<State, Action, Effect, Event>(State()) {

    private val userRepository: UserRepository = UserRepositoryImpl()

    override fun reduce(action: Action, state: State): Update<State, Effect, Event> = when (action) {
        is Action.Load -> {
            Update(state.copy(isLoading = true), Effect.FetchUsers)
        }
        is Action.CreateUser -> {
            val name = state.name.text.trim()
            val surname = state.surname.text.trim()

            if (name.isEmpty() || surname.isEmpty()) {
                Update(state, Event.ShowError("Fill entries"))
            }
            else {
                Update(
                    state.copy(
                        name = TextFieldValue(),
                        surname = TextFieldValue(),
                        isAddEnabled = false
                    ),
                    Effect.PostUser(state.name.text, state.surname.text)
                )
            }
        }
        is Action.UserListChanged -> {
            Update(state.copy(isLoading = false, users = action.users))
        }
        is Action.UserAppended -> {
            Update(state.copy(isAddEnabled = true, users = state.users + action.user))
        }
        is Action.NameChanged -> {
            Update(state.copy(name = action.value))
        }
        is Action.SurnameChanged -> {
            Update(state.copy(surname = action.value))
        }
        is Action.TriggerFakeEvent -> {
            Update(state, Event.FakeNavigate)
        }
        else -> {
            Update(state)
        }
    }

    override fun affect(effect: Effect): Flow<EffectResult<Action, Event>> = flow {
        when (effect) {
            Effect.FetchUsers -> {
                userRepository.getUsers().collect {
                    emit(Action.UserListChanged(it).asResult())
                }
            }
            is Effect.PostUser -> {
                userRepository.createUser(effect.name, effect.username).collect {
                    emit(Action.UserAppended(it).asResult())
                }
            }
            else -> {
            }
        }
    }
}