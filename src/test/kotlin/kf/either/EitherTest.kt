package kf.either

import com.github.rinotc.kf.either.Either
import com.github.rinotc.kf.either.Either.Companion.contains
import com.github.rinotc.kf.either.Either.Companion.flatMap
import com.github.rinotc.kf.either.Either.Companion.getOrElse
import com.github.rinotc.kf.either.Either.Companion.merge
import com.github.rinotc.kf.either.Either.Companion.orElse
import com.github.rinotc.kf.either.Left
import com.github.rinotc.kf.either.Right
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EitherTest {

    @Nested
    @DisplayName("#isLeft")
    inner class IsLeft {

        @Test
        fun `should return true when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            assertTrue(either.isLeft())
        }

        @Test
        fun `should return false when the Either is Right`() {
            val either: Either<String, Int> = Either.right(5)
            assertFalse(either.isLeft())
        }
    }

    @Nested
    @DisplayName("#isRight")
    inner class IsRight {

        @Test
        fun `should return false when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            assertFalse(either.isRight())
        }

        @Test
        fun `should return true when the Either is Right`() {
            val either: Either<String, Int> = Either.right(5)
            assertTrue(either.isRight())
        }
    }

    @Nested
    @DisplayName("#fold")
    inner class Fold {

        @Test
        fun `should return the result of the left function when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            val result = either.fold(
                { it.length },
                { it * 2 }
            )
            assert(result == 5)
        }

        @Test
        fun `should return the result of the right function when the Either is Right`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.fold(
                { it.length },
                { it * 2 }
            )
            assert(result == 10)
        }
    }

    @Nested
    @DisplayName("#swap")
    inner class Swap {

        @Test
        fun `should return Right with the value of Left when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            val result = either.swap()
            assert(result is Right)
            assert(result.get() == "Error")
        }

        @Test
        fun `should return Left with the value of Right when the Either is Right`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.swap()
            assert(result is Left)
            assert(result.left().get() == 5)
        }
    }

    @Nested
    @DisplayName("#foreach")
    inner class Foreach {

        @Test
        fun `should not execute the function when the Either is Left`() {
            var executed = false
            val either: Either<String, Int> = Either.left("Error")
            either.foreach { executed = true }
            assert(!executed)
        }

        @Test
        fun `should execute the function when the Either is Right`() {
            var executed = false
            val either: Either<String, Int> = Either.right(5)
            either.foreach { executed = true }
            assert(executed)
        }
    }

    @Nested
    @DisplayName("#get")
    inner class Get {

        @Test
        fun `should throw NoSuchElementException when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            assertThrows<NoSuchElementException> { either.get() }
        }

        @Test
        fun `should return the value when the Either is Right`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.get()
            assert(result == 5)
        }
    }

    @Nested
    @DisplayName("#getOrElse")
    inner class GetOrElse {

        @Test
        fun `should return the value when the Either is Right`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.getOrElse { 0 }
            assert(result == 5)
        }

        @Test
        fun `should return the default value when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            val result = either.getOrElse { 0 }
            assert(result == 0)
        }
    }

    @Nested
    @DisplayName("#orElse")
    inner class OrElse {

        @Test
        fun `should return the Either when the Either is Right`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.orElse { Either.right(0) }
            assert(result is Right)
            assert(result.get() == 5)
        }

        @Test
        fun `should return the default Either when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            val result = either.orElse { Either.right(0) }
            assert(result is Right)
            assert(result.get() == 0)
        }
    }

    @Nested
    @DisplayName("#merge")
    inner class Merge {

        @Test
        fun `can unwrap type when Left and Right type is same`() {
            // when Right.
            val either: Either<String, String> = Either.right("Hello")
            val result = either.merge()
            assert(result == "Hello")

            // when Left.
            val either2: Either<String, String> = Either.left("Error")
            val result2 = either2.merge()
            assert(result2 == "Error")
        }
    }

    @Nested
    @DisplayName("#contains")
    inner class Contains {

        @Test
        fun `should return false when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            assertFalse { either.contains(5) }
        }

        @Test
        fun `should return true when the Either is Right and the value is the same`() {
            val either: Either<String, Int> = Either.right(5)
            assertTrue { either.contains(5) }
        }

        @Test
        fun `should return false when the Either is Right and the value is different`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.contains(10)
            assertFalse { either.contains(10) }
        }
    }

    @Nested
    @DisplayName("#flatMap")
    inner class FlatMap {

        @Test
        fun `should return the result of the function when the Either is Right`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.flatMap { Either.right(it * 2) }
            assert(result is Right)
            assert(result.get() == 10)
        }

        @Test
        fun `should return the Either when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            val result = either.flatMap { Either.right(it * 2) }
            assert(result is Left)
            assert(result.left().get() == "Error")
        }
    }

    @Nested
    @DisplayName("#forall")
    inner class Forall {

        @Test
        fun `should return true when the Either is Right and the predicate is true`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.forall { it == 5 }
            assert(result)
        }

        @Test
        fun `should return false when the Either is Right and the predicate is false`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.forall { it == 10 }
            assert(!result)
        }

        @Test
        fun `should return true when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            val result = either.forall { it == 5 }
            assert(result)
        }
    }

    @Nested
    @DisplayName("#exists")
    inner class Exists {

        @Test
        fun `should return true when the Either is Right and the predicate is true`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.exists { it == 5 }
            assert(result)
        }

        @Test
        fun `should return false when the Either is Right and the predicate is false`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.exists { it == 10 }
            assert(!result)
        }

        @Test
        fun `should return false when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            val result = either.exists { it == 5 }
            assert(!result)
        }
    }

    @Nested
    @DisplayName("#map")
    inner class Map {

        @Test
        fun `should return the result of the function when the Either is Right`() {
            val either: Either<String, Int> = Either.right(5)
            val result = either.map { it * 2 }
            assert(result is Right)
            assert(result.get() == 10)
        }

        @Test
        fun `should return the Either when the Either is Left`() {
            val either: Either<String, Int> = Either.left("Error")
            val result = either.map { it * 2 }
            assert(result is Left)
            assert(result.left().get() == "Error")
        }
    }
}