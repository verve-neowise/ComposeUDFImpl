package io.neowise.android.composeudf.ui.screens.user_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.neowise.android.composeudf.R

@Composable
fun UserItem(
    name: String,
    surname: String,
) {
    Column(
        modifier = Modifier

            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .size(48.dp),

                painter = painterResource(id = R.drawable.profile_placeholder),
                contentDescription = null
            )
            Text(
                modifier = Modifier,
                text = "$name $surname",
                fontSize = 19.sp
            )
        }
        Divider(color = Color.Black.copy(0.1f))
    }
}
