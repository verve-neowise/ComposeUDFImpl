package io.neowise.android.composeudf.core.udf

class NotDispatchedException(message: String) : Exception(message)
class MustBeOverrideException(message: String) : Exception(message)

fun ActionMustBeOvveride(action: UDF.Action): Nothing {
    val className = action::class.java.canonicalName ?: action.toString()
    throw NotDispatchedException("Action must be override: $className")
}

fun EffectMustBeOvveride(effect: UDF.Effect): Nothing {
    val className = effect::class.java.canonicalName ?: effect.toString()
    throw NotDispatchedException("Effect must be override: $className")
}

fun ActionNotDispatched(action: UDF.Action): Nothing {
    val className = action::class.java.canonicalName ?: action.toString()
    throw NotDispatchedException("Action do not dispatched: $className")
}

fun EffectNotDispatched(effect: UDF.Effect): Nothing {
    val className = effect::class.java.canonicalName ?: effect.toString()
    throw NotDispatchedException("Effect do not dispatched: $className")
}