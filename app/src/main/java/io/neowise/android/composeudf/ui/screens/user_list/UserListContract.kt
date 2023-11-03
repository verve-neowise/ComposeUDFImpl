package io.neowise.android.composeudf.ui.screens.user_list

import androidx.compose.ui.text.input.TextFieldValue
import io.neowise.android.composeudf.domain.User
import io.neowise.android.composeudf.core.udf.UDF

class UserListContract {

    data class State(
        val isLoading: Boolean = false,
        val isAddEnabled: Boolean = true,
        val error: String? = null,
        val users: List<User> = listOf(),
        val name: TextFieldValue = TextFieldValue(),
        val surname: TextFieldValue = TextFieldValue(),
    ) : UDF.State

    interface Action : UDF.Action {
        object Load : Action
        object CreateUser : Action
        data class UserAppended(val user: User) : Action
        data class UserListChanged(val users: List<User>) : Action
        data class NameChanged(val value: TextFieldValue) : Action
        data class SurnameChanged(val value: TextFieldValue) : Action
    }

    interface Effect : UDF.Effect {
        object FetchUsers : Effect
        data class PostUser(val name: String, val username: String) : Effect
    }

    interface Event : UDF.Event {
        data class OnShowError(val message: String) : Event
        object OnCloseApp : Event
    }
}