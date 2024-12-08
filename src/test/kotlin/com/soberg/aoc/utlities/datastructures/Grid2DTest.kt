package com.soberg.aoc.utlities.datastructures

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.containsOnly
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.soberg.aoc.utlities.datastructures.Grid2D.Direction
import com.soberg.aoc.utlities.datastructures.Grid2D.Location
import com.soberg.aoc.utlities.datastructures.Grid2D.Location.Companion.loc
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
    fun `return expected element for get row, col`() {
        assertThat(testGrid[3, 3])
            .isEqualTo(4444)
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
    fun `return true when location in bounds for contains check`() {
        assertThat(Location(2, 2) in testGrid)
            .isTrue()
    }

    @Test
    fun `return false when location not in bounds`() {
        assertThat(testGrid.isInBounds(Location(5, 1)))
            .isFalse()
    }

    @Test
    fun `return false when location not in bounds for contains check`() {
        assertThat(Location(5, 1) in testGrid)
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
    fun `return expected elements for collect`() {
        val actual = testGrid.collect(Location(2, 1), Direction.East, 3)
        assertThat(actual)
            .isNotNull()
            .containsExactly(222, 333, 444)
    }

    @Test
    fun `return null for collect when out of bounds`() {
        val actual = testGrid.collect(Location(2, 1), Direction.East, 4)
        assertThat(actual)
            .isNull()
    }

    @Test
    fun `return expected element map with locations`() {
        val grid = listOf(
            listOf(1, 2, 3),
            listOf(3, 2, 1),
            listOf(2, 3, 1),
        ).toGrid2D()
        assertThat(grid.elementToLocationsMap())
            .containsOnly(
                1 to listOf(0 loc 0, 1 loc 2, 2 loc 2),
                2 to listOf(0 loc 1, 1 loc 1, 2 loc 0),
                3 to listOf(0 loc 2, 1 loc 0, 2 loc 1),
            )
    }

    @Test
    fun `return expected filtered map with locations`() {
        val grid = listOf(
            listOf(1, 2, 3),
            listOf(3, 2, 1),
            listOf(2, 3, 1),
        ).toGrid2D()
        assertThat(grid.filterElementToLocationsMap { _, loc -> loc.col == 1 })
            .containsOnly(
                2 to listOf(0 loc 1, 1 loc 1),
                3 to listOf(2 loc 1),
            )
    }

    @Test
    fun `touch all locations in grid for traverse`() {
        val grid = listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
        ).toGrid2D()
        val actualTouched = buildList {
            grid.traverse {
                add(it.row to it.col)
            }
        }
        assertThat(actualTouched)
            .containsExactly(
                0 to 0, 0 to 1, 0 to 2,
                1 to 0, 1 to 1, 1 to 2,
            )
    }

    @Test
    fun `touch all expected locations in grid for traverse direction`() {
        val grid = listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
        ).toGrid2D()
        val actualTouched = buildList {
            grid.traverseDirection(0 loc 0, Direction.East) {
                add(it.row to it.col)
            }
        }
        assertThat(actualTouched)
            .containsExactly(
                0 to 0, 0 to 1, 0 to 2,
            )
    }

    @Test
    fun `create expected grid from input`() {
        val grid = listOf(
            "ABC",
            "123",
        ).toGrid2D { line ->
            line.toCharArray().asList()
        }
        assertThat(grid)
            .isEqualTo(
                Grid2D(
                    listOf(
                        listOf('A', 'B', 'C'),
                        listOf('1', '2', '3'),
                    )
                )
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

    @Test
    fun `create expected output string`() {
        val grid = listOf(
            "ABC",
            "123",
            "UNM",
        ).toCharGrid2D()
        assertThat(grid.toString())
            .isEqualTo(
                "ABC\n123\nUNM\n"
            )
    }

    @Test
    fun `create expected output string when filtering`() {
        val grid = listOf(
            listOf(1,2,3),
            listOf(4,5,6),
        ).toGrid2D()
        assertThat(grid.toString { "${grid[it] + 1}," })
            .isEqualTo(
                "2,3,4,\n5,6,7,\n"
            )
    }
}