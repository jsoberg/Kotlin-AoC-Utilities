package com.soberg.aoc.utlities.extensions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class AsyncCountTest {
    private val testData = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

    @Test
    fun `return count - blocking`() {
        val underFive = testData.asyncCountBlocking {
            it < 5
        }

        assertThat(underFive).isEqualTo(5)
    }

    @Test
    fun `return count - coroutine`() = runTest {
        val overFive = testData.asyncCount {
            it > 5
        }

        assertThat(overFive).isEqualTo(4)
    }
}