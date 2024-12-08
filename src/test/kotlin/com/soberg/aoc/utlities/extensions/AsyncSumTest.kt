package com.soberg.aoc.utlities.extensions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class AsyncSumTest {
    private val testData = "The quick brown fox jumped over the lazy dog".split(" ")

    @Test
    fun `return sum of integers - blocking`() {
        val letterCount = testData.asyncSumOfBlocking {
            it.length
        }

        assertThat(letterCount).isEqualTo(36)
    }
    
    @Test
    fun `return sum of integers - coroutine`() = runTest {
        val letterCount = testData.asyncSumOf {
            it.length
        }

        assertThat(letterCount).isEqualTo(36)
    }

    @Test
    fun `return sum of longs - blocking`() {
        val letterCount = testData.asyncSumOfBlocking {
            it.length.toLong()
        }

        assertThat(letterCount).isEqualTo(36)
    }

    @Test
    fun `return sum of longs - coroutine`() = runTest {
        val letterCount = testData.asyncSumOf {
            it.length.toLong()
        }

        assertThat(letterCount).isEqualTo(36)
    }
}
