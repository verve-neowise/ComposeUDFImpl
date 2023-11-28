package io.neowise.android.composeudf.core.udf

interface UDF {
    interface State
    interface Action
    interface Event
    interface Effect
}

data class Update<S: UDF.State, EF: UDF.Effect, E: UDF.Event>(val state: S?, val effects: List<EF> = listOf(), val events: List<E> = listOf()) {
    constructor(): this(null, listOf())
    constructor(state: S, effect: EF): this(state, listOf(effect))
    constructor(state: S, event: E): this(state, listOf(), listOf(event))
    constructor(state: S, effect: EF, event: E): this(state, listOf(effect), listOf(event))
    constructor(state: S, effect: EF, vararg events: E): this(state, listOf(effect), listOf(*events))
    constructor(state: S, vararg effects: EF): this(state, listOf(*effects))
    constructor(state: S, effects: List<EF>, event: E): this(state, effects, listOf(event))
}

data class EffectResult<A: UDF.Action, E: UDF.Event>(val action: A? = null, val events: List<E> = listOf()) {
    constructor(action: A? = null, event: E): this(action, listOf(event))
    constructor(action: A? = null, vararg events: E): this(action, listOf(*events))
    constructor(event: E): this(null, listOf(event))
}

fun <A : UDF.Action, E: UDF.Event> A.asResult(): EffectResult<A, E> {
    return EffectResult(this)
}

fun <A : UDF.Action, E: UDF.Event> E.asResult(): EffectResult<A, E> {
    return EffectResult(this)
}

fun <A: UDF.Action, E: UDF.Event> Nothing() = EffectResult<A, E>(null)