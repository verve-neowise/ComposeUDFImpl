package io.neowise.android.composeudf.ui.screens.test

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TestScreen() {
    Column {
        Card(
            modifier = Modifier
                .padding(16.dp, 8.dp),
            colors = CardDefaults.cardColors(contentColor = Color.LightGray),
            shape = RoundedCornerShape(4.dp)
        ) {

        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            shape = MaterialTheme.shapes.large
        ) {
        }
    }
}