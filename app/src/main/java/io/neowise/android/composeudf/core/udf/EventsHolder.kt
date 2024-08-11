package io.neowise.android.composeudf.core.udf

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow


@Immutable
class EventsHolder<T>(
    val events: Flow<T> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
)