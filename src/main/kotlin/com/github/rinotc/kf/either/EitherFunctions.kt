package com.github.rinotc.com.github.rinotc.kf.either

object EitherFunctions {

    fun <L, R> Either<L, R>.get(): R = when (this) {
        is Left<L> -> throw NoSuchElementException("Left has no value")
        is Right<R> -> this.value
    }

    fun <L, R> Either<L, R>.getLeft() = when (this) {
        is Left<L> -> this.value
        is Right<R> -> throw NoSuchElementException("Right has no value")
    }

    fun <L, R, RR> Either<L, R>.map(f: (R) -> RR): Either<L, RR> = when (this) {
        is Left<L> -> this
        is Right<R> -> Right(f(this.value))
    }

    fun <L, R, LL> Either<L, R>.mapLeft(f: (L) -> LL): Either<LL, R> = when (this) {
        is Left<L> -> Left(f(this.value))
        is Right<R> -> this
    }

    fun <L, R, RR> Either<L, R>.flatMap(f: (R) -> Either<L, RR>): Either<L, RR> = when (this) {
        is Left<L> -> this
        is Right<R> -> f(this.value)
    }

    fun <L, R> Either<L, R>.getOrElse(default: () -> R): R = when (this) {
        is Left<L> -> default()
        is Right<R> -> this.value
    }

    fun <L, R> Either<L, R>.getOrElse(default: R): R = when (this) {
        is Left<L> -> default
        is Right<R> -> this.value
    }

    fun <L, R> Either<L, R>.orElse(default: () -> Either<L, R>): Either<L, R> = when (this) {
        is Left<L> -> default()
        is Right<R> -> this
    }
}