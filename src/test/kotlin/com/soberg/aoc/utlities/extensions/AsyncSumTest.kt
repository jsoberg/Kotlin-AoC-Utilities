package com.soberg.aoc.utlities.extensions

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class AsyncSumTest {
    private val testData = "The quick brown fox jumped over the lazy dog".split(" ")

    @Test
    fun `return sum of integers`() {
        val letterCount = testData.asyncSumOf {
            it.length
        }

        assertThat(letterCount).isEqualTo(36)
    }

    @Test
    fun `return sum of longs`() {
        val letterCount = testData.asyncSumOf {
            it.length.toLong()
        }

        assertThat(letterCount).isEqualTo(36)
    }
}
