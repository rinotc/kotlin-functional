package com.github.rinotc.kf.either

sealed interface Either<out L, out R> {

    fun left(): LeftProjection<L, R> = LeftProjection(this)

    fun isLeft(): Boolean = this is Left

    fun isRight(): Boolean = this is Right

    fun <A> fold(ifLeft: (L) -> A, ifRight: (R) -> A): A = when (this) {
        is Left -> ifLeft(this.value)
        is Right -> ifRight(this.value)
    }

    fun swap(): Either<R, L> = when (this) {
        is Left -> Right(this.value)
        is Right -> Left(this.value)
    }

    fun foreach(f: (R) -> Unit) = when (this) {
        is Left -> Unit
        is Right -> f(this.value)
    }

    fun get(): R = when (this) {
        is Left -> throw NoSuchElementException("Left has no value")
        is Right -> this.value
    }

    fun forall(f: (R) -> Boolean): Boolean = when (this) {
        is Left -> true
        is Right -> f(this.value)
    }

    fun exists(f: (R) -> Boolean): Boolean = when (this) {
        is Left -> false
        is Right -> f(this.value)
    }

    fun <RR> map(f: (R) -> RR): Either<L, RR> = when (this) {
        is Left -> this
        is Right -> Right(f(this.value))
    }

    companion object {
        fun <L, R> cond(condition: Boolean, ifTrue: R, ifFalse: L): Either<L, R> {
            return if (condition) Right(ifTrue) else Left(ifFalse)
        }

        fun <L, R> fromNullable(value: R?, ifNull: () -> L): Either<L, R> {
            return if (value != null) Right(value) else Left(ifNull())
        }

        fun <R> right(value: R): Either<Nothing, R> = Right(value)

        fun <L> left(value: L): Either<L, Nothing> = Left(value)

        fun <L, R> Either<L, R>.getOrElse(default: () -> R) = when (this) {
            is Left -> default()
            is Right -> this.value
        }

        fun <L, R> Either<L, R>.orElse(default: () -> Either<L, R>) = when (this) {
            is Left -> default()
            is Right -> this
        }

        fun <R> Either<R, R>.merge(): R = when (this) {
            is Left -> this.value
            is Right -> this.value
        }

        fun <L, R, RR> Either<L, R>.flatMap(f: (R) -> Either<L, RR>): Either<L, RR> = when (this) {
            is Left -> this
            is Right -> f(this.value)
        }

        fun <L, R> Either<L, R>.contains(elem: R): Boolean = when (this) {
            is Left -> false
            is Right -> this.value == elem
        }
    }
}

data class Left<L>(val value: L) : Either<L, Nothing>
data class Right<R>(val value: R) : Either<Nothing, R>

class LeftProjection<out L, out R>(private val e: Either<L, R>) {
    fun get(): L = when (e) {
        is Left -> e.value
        is Right -> throw NoSuchElementException("Right has no value")
    }

    fun foreach(f: (L) -> Unit) = when (e) {
        is Left -> f(e.value)
        is Right -> Unit
    }

    fun <LL> map(f: (L) -> LL): Either<LL, R> = when (e) {
        is Left -> Left(f(e.value))
        is Right -> e
    }

    fun forall(f: (L) -> Boolean): Boolean = when (e) {
        is Left -> f(e.value)
        is Right -> true
    }

    fun exists(f: (L) -> Boolean): Boolean = when (e) {
        is Left -> f(e.value)
        is Right -> false
    }

    companion object {
        fun <L, R> LeftProjection<L, R>.getOrElse(default: () -> L) = when(this.e) {
            is Left -> this.e.value
            is Right -> default()
        }

        fun <L, R> LeftProjection<L, R>.flatMap(f: (L) -> Either<L, R>): Either<L, R> = when(this.e) {
            is Left -> f(this.e.value)
            is Right -> this.e
        }
    }
}