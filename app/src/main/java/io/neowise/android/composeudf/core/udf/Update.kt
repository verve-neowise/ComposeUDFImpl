package io.neowise.android.composeudf.core.udf

data class Update<S: UDF.State, EF: UDF.Effect>(val state: S, val effect: Set<EF>)

infix fun <S: UDF.State, EF: UDF.Effect> S.with(effect: EF) = Update(this, setOf(effect))
infix fun <S: UDF.State, EF: UDF.Effect> S.with(effects: Set<EF>) = Update(this, effects)
fun <S: UDF.State, EF: UDF.Effect> S.only() = Update<S, EF>(this, setOf())
