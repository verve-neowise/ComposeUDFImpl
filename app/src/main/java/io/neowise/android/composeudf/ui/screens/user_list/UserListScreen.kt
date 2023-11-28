package io.neowise.android.composeudf.ui.screens.user_list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.neowise.android.composeudf.core.OnEvent
import io.neowise.android.composeudf.core.callback
import io.neowise.android.composeudf.domain.User
import io.neowise.android.composeudf.core.udf.UDF
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.Action
import io.neowise.android.composeudf.ui.screens.user_list.UserListContract.Event
import io.neowise.android.composeudf.ui.screens.user_list.components.UserItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    state: UserListContract.State,
    events: Flow<UDF.Event>,
    dispatch: (Action) -> Unit,
    closeApp: () -> Unit,
    navigateNext: () -> Unit,
) {
    val context = LocalContext.current

    OnEvent(events = events) { event ->
        when(event) {
            is Event.ShowError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
            Event.OnCloseApp -> {
                closeApp()
            }
            Event.FakeNavigate -> {
                navigateNext()
            }
        }
    }

    Scaffold(
        floatingActionButton = {
           FloatingActionButton(
               onClick = callback {
                   dispatch(Action.TriggerFakeEvent)
               }
           ) {
               Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
           }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = state.name,
                    onValueChange = callback { value ->
                        dispatch(Action.NameChanged(value))
                    }
                )
                TextField(
                    modifier = Modifier.weight(1f),
                    value = state.surname,
                    onValueChange = callback { value ->
                        dispatch(Action.SurnameChanged(value))
                    }
                )
                IconButton(
                    modifier = Modifier,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Black.copy(0.9f)
                    ),
                    enabled = state.isAddEnabled,
                    onClick = callback {
                        dispatch(Action.CreateUser)
                    },
                ) {
                   Icon(Icons.Default.Check, contentDescription = null)
                }
            }
        },
        content = { padding ->
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else {
                LazyColumn(
                    modifier = Modifier.padding(padding)
                ) {
                    items(state.users, { it.name + it.surname }) { user ->
                        UserItem(name = user.name, surname = user.surname)
                    }
                }
            }
        }
    )
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xffffffff)
private fun DefaultPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        UserListScreen(
            state = UserListContract.State(
                users = listOf(
                    User("Jason", "Stathem")
                )
            ),
            events = flowOf(),
            dispatch = {
            },
            closeApp = {
            },
            navigateNext = {

            }
        )
    }
}
