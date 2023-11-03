package io.neowise.android.composeudf.ui.screens.test

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun TestScreen() {
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
        Text(
            text = "Test Screen",
            fontSize = 26.sp
        )
    }
}