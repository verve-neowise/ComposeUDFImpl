package io.neowise.android.composeudf.ui.screens.user_list.profile_info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.neowise.android.composeudf.R
import io.neowise.android.composeudf.ui.screens.user_list.profile_info.ProfileInfoContract.Action
import io.neowise.android.composeudf.ui.screens.user_list.profile_info.ProfileInfoContract.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfo(
    modifier: Modifier = Modifier,
    state: State = State(),
    dispatch: (Action) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(100))
                    .align(Alignment.CenterHorizontally)
                ,
                painter = painterResource(id = R.drawable.profile_placeholder),
                contentDescription = null
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                readOnly = !state.isEdit,
                label = {
                    Text(text = "Name")
                },
                onValueChange = {
                    dispatch(Action.NameChanged(it))
                }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.email,
                readOnly = !state.isEdit,
                label = {
                    Text(text = "Email")
                },
                onValueChange = {
                    dispatch(Action.EmailChanged(it))
                }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Switch(
                    checked = state.isEdit,
                    onCheckedChange = {
                        dispatch(Action.ToggleEdit)
                    }
                )
                Text(text = "Enable edit")
            }
        }
    }
}

// preview

@Composable
@Preview(backgroundColor = 0xffffffff, showBackground = true)
fun ProfileInfoPreview() {
    MaterialTheme {
        ProfileInfo(
            state = State(),
            dispatch = {}
        )
    }
}