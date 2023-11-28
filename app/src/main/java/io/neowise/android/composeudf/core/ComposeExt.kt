package io.neowise.android.composeudf.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

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
fun callback(key: Any?, callback: () -> Unit): () -> Unit {
    return remember {
        callback
    }
}

@Composable
fun <F> callback(key: Any?, callback: (F) -> Unit): (F) -> Unit {
    return remember {
        callback
    }
}

@Composable
fun <F, S> callback(key: Any?, callback: (F, S) -> Unit): (F, S) -> Unit {
    return remember {
        callback
    }
}

@Composable
fun <F, S, T> callback(key: Any?, callback: (F, S, T) -> Unit): (F, S, T) -> Unit {
    return remember {
        callback
    }
}

@Composable
fun <T> OnEvent(events: Flow<T>, handler: (event: T) -> Unit) {
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            events
                .onEach(handler)
                .collect()
        }
    }
}