package com.github.rinotc

import com.github.rinotc.com.github.rinotc.kf.either.Either
import com.github.rinotc.com.github.rinotc.kf.either.Left
import com.github.rinotc.com.github.rinotc.kf.either.Right
import com.github.rinotc.com.github.rinotc.kf.either.either

fun main() {
    val a: Either<String, Int> = Right(1)
    val b: Either<String, Int> = Right(2)
    val c: Either<String, Int> = Left("Error Occurred.")
    val result = either {
        val x = a.bind()
        val y = b.bind()
        val z = c.bind()
        x + y + z
    }
    println(result)
}