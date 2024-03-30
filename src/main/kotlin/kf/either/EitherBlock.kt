package com.github.rinotc.kf.either

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class)
inline fun <L, R> either(crossinline block: EitherBinding<L>.() -> R): Either<L, R> {

    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val receiver = EitherBindingImpl<L>()

    return try {
        with(receiver) { Right(block()) }
    } catch (ex: BindException) {
        receiver.left
    }
}

class BindException : Exception()

interface EitherBinding<L> {
    fun <R> Either<L, R>.bind(): R
}

@PublishedApi
internal class EitherBindingImpl<L> : EitherBinding<L> {

    lateinit var left: Left<L>

    override fun <R> Either<L, R>.bind(): R {
        return when (this) {
            is Right -> value
            is Left -> {
                this@EitherBindingImpl.left = this
                throw BindException()
            }
        }
    }
}
