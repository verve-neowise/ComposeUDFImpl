package io.neowise.android.composeudf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.neowise.android.composeudf.core.callback
import io.neowise.android.composeudf.ui.screens.test.TestScreen
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.Action
import io.neowise.android.composeudf.ui.screens.user_list.UserListScreen
import io.neowise.android.composeudf.ui.screens.user_list.UserListViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: UserListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NavHost(navController, "users") {
                composable("users") {
                    UserListScreen(
                        state = viewModel.state.value,
                        events = viewModel.events,
                        dispatch = viewModel::dispatch,
                        closeApp = ::finish,
                        navigateNext = callback {
                            navController.navigate("test")
                        },
                    )
                }
                composable("test") {
                    TestScreen()
                }

            }
        }
        viewModel.dispatch(Action.Load)
    }
}

