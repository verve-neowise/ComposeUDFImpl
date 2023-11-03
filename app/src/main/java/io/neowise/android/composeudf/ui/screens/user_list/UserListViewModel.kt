package io.neowise.android.composeudf.ui.screens.user_list

import androidx.compose.ui.text.input.TextFieldValue
import io.neowise.android.composeudf.data.UserRepositoryImpl
import io.neowise.android.composeudf.domain.UserRepository
import io.neowise.android.composeudf.core.udf.UDFViewModel
import io.neowise.android.composeudf.core.udf.Update
import io.neowise.android.composeudf.core.udf.only
import io.neowise.android.composeudf.core.udf.with
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserListViewModel : UDFViewModel<Action, Effect, State>(State()) {

    private val userRepository: UserRepository = UserRepositoryImpl()

    override fun reduce(action: Action, state: State): Update<State, Effect> = when (action) {
        is Action.Load -> state.copy(isLoading = true) with Effect.FetchUsers
        is Action.CreateUser -> createUser(state)
        is Action.UserListChanged -> state.copy(isLoading = false, users = action.users).only()
        is Action.UserAppended -> state.copy(isAddEnabled = true, users = state.users + action.user).only()
        is Action.NameChanged -> state.copy(name = action.value).only()
        is Action.SurnameChanged -> state.copy(surname = action.value).only()
        else -> state.only()
    }

    private fun createUser(state: State): Update<State, Effect> {
        val name = state.name.text.trim()
        val surname = state.surname.text.trim()

        return if (name.isEmpty() || surname.isEmpty()) {
            event(Event.OnShowError("Fill entries"))
            state.only()
        }
        else {
            state.copy(
                name = TextFieldValue(),
                surname = TextFieldValue(),
                isAddEnabled = false
            ) with Effect.PostUser(state.name.text, state.surname.text)
        }
    }

    override fun effect(effect: Effect): Flow<Action> = flow {
        when (effect) {
            Effect.FetchUsers -> {
                userRepository.getUsers().collect {
                    emit(Action.UserListChanged(it))
                }
            }
            is Effect.PostUser -> {
                userRepository.createUser(effect.name, effect.username).collect {
                    emit(Action.UserAppended(it))
                }
            }
            else -> {
            }
        }
    }
}