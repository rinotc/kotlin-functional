package com.github.rinotc.kf.either

sealed interface Either<out L, out R> {

    companion object {
        fun <LL, RR> cond(condition: Boolean, ifTrue: RR, ifFalse: LL): Either<LL, RR> {
            return if (condition) Right(ifTrue) else Left(ifFalse)
        }

        fun <LL, RR> fromNullable(value: RR?, ifNull: () -> LL): Either<LL, RR> {
            return if (value != null) Right(value) else Left(ifNull())
        }

        fun <RR> right(value: RR): Either<Nothing, RR> = Right(value)

        fun <LL> left(value: LL): Either<LL, Nothing> = Left(value)
    }
}

data class Left<L>(val value: L) : Either<L, Nothing>
data class Right<R>(val value: R) : Either<Nothing, R>