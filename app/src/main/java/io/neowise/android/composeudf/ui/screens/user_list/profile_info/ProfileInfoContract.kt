package io.neowise.android.composeudf.ui.screens.user_list.profile_info

import androidx.compose.ui.text.input.TextFieldValue
import io.neowise.android.composeudf.core.udf.UDF

interface ProfileInfoContract {

    data class State(
        val avatar: String = "",
        val name: TextFieldValue = TextFieldValue(),
        val email: TextFieldValue = TextFieldValue(),
        val isEdit: Boolean = false
    ) : UDF.State

    interface Action : UDF.Action {
        object ToggleEdit : Action
        data class NameChanged(val value: TextFieldValue) : Action
        data class EmailChanged(val value: TextFieldValue) : Action
    }
}