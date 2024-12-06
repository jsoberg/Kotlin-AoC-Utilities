package com.soberg.aoc.utlities.datastructures

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.soberg.aoc.utlities.datastructures.Grid2D.Direction
import com.soberg.aoc.utlities.datastructures.Grid2D.Location
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Grid2DTest {

    private val testGrid = listOf(
        listOf(1, 2, 3, 4),
        listOf(11, 22, 33, 44),
        listOf(111, 222, 333, 444),
        listOf(1111, 2222, 3333, 4444),
        listOf(11111, 22222, 33333, 44444),
    ).toGrid2D()

    @Test
    fun `return expected row size`() {
        assertThat(testGrid.rowSize).isEqualTo(5)
    }

    @Test
    fun `return expected col size`() {
        assertThat(testGrid.colSize).isEqualTo(4)
    }

    @Test
    fun `return expected element for get`() {
        assertThat(testGrid[Location(2, 2)])
            .isEqualTo(333)
    }

    @Test
    fun `return expected element for elementAt`() {
        val actual = testGrid.elementAt(Location(2, 2), Direction.NorthWest, 2)
        assertThat(actual)
            .isEqualTo(1)
    }

    @Test
    fun `return null for elementAt when out of bounds`() {
        val actual = testGrid.elementAt(Location(2, 2), Direction.NorthWest, 3)
        assertThat(actual)
            .isNull()
    }

    @Test
    fun `return true when location in bounds`() {
        assertThat(testGrid.isInBounds(Location(2, 2)))
            .isTrue()
    }

    @Test
    fun `return false when location not in bounds`() {
        assertThat(testGrid.isInBounds(Location(5, 1)))
            .isFalse()
    }

    @Test
    fun `return replaced element in new grid for in bounds location`() {
        val actual = testGrid.replace(Location(2, 2), 100000)
        assertThat(actual)
            .isEqualTo(
                listOf(
                    listOf(1, 2, 3, 4),
                    listOf(11, 22, 33, 44),
                    listOf(111, 222, 100000, 444),
                    listOf(1111, 2222, 3333, 4444),
                    listOf(11111, 22222, 33333, 44444),
                ).toGrid2D()
            )
    }

    @Test
    fun `throw when attempting replace for out of bounds location`() {
        val exception = assertThrows<IllegalArgumentException> {
            testGrid.replace(Location(12, 2), 100000)
        }
        assertThat(exception)
            .hasMessage("${Location(12, 2)} not in bounds for this grid")
    }

    @Test
    fun `throw for non-uniform grid`() {
        val exception = assertThrows<IllegalStateException> {
            listOf(
                listOf(1, 2, 3, 4),
                listOf(11, 22, 33, 44),
                listOf(111, 222, 333, 444),
                listOf(1111, 2222, 3333, 4444, 5555),
                listOf(11111, 22222, 33333, 44444),
            ).toGrid2D()
        }
        assertThat(exception)
            .hasMessage("Grid is not uniform - row at index 3 has a differing # of columns")
    }
}