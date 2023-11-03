package io.neowise.android.composeudf.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@Composable
fun callback(callback: () -> Unit): () -> Unit {
    return remember {
        callback
    }
}

@Composable
fun <F> callback(callback: (F) -> Unit): (F) -> Unit {
    return remember {
        callback
    }
}

@Composable
fun <F, S> callback(callback: (F, S) -> Unit): (F, S) -> Unit {
    return remember {
        callback
    }
}

@Composable
fun <F, S, T> callback(callback: (F, S, T) -> Unit): (F, S, T) -> Unit {
    return remember {
        callback
    }
}

@Composable
fun <T> OnEvent(events: Flow<T>, handler: (event: T) -> Unit) {
    LaunchedEffect(Unit) {
        events
            .onEach(handler)
            .collect()
    }
}