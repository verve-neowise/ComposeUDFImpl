package io.neowise.android.composeudf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.Action
import io.neowise.android.composeudf.ui.screens.user_list.UserListScreen
import io.neowise.android.composeudf.ui.screens.user_list.UserListViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: UserListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UserListScreen(
                state = viewModel.state.value,
                events = viewModel.events,
                sendAction = viewModel::action,
                closeApp = ::finish
            )
        }
        viewModel.action(Action.Load)
    }
}