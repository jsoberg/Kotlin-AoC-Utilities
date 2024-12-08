package com.soberg.aoc.utlities.datastructures

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.soberg.aoc.utlities.datastructures.Grid2D.Direction
import com.soberg.aoc.utlities.datastructures.Grid2D.Location
import com.soberg.aoc.utlities.datastructures.Grid2D.Location.Distance
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class Grid2DLocationTest {

    @ParameterizedTest
    @MethodSource("provideArgumentsForDistanceOfOneMove")
    fun `return expected location for move with no specified distance`(
        direction: Direction,
        expected: Location,
    ) {
        val actual = origin.move(direction)
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForDistanceOfNineMove")
    fun `return expected location for move with specified distance`(
        direction: Direction,
        expected: Location,
    ) {
        val actual = origin.move(direction, 9)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `return expected distance for plus location`() {
        assertThat(Location(10, 10) distanceTo Location(1, 2))
            .isEqualTo(Distance(9, 8))

        assertThat(Location(1, 2) distanceTo Location(10, 10))
            .isEqualTo(Distance(-9, -8))
    }

    @Test
    fun `return expected location when adding distance`() {
        assertThat(Location(10, 10) + Distance(1, 2))
            .isEqualTo(Location(11, 12))
    }

    @Test
    fun `return expected location when subtracting distance`() {
        assertThat(Location(10, 10) - Distance(1, 2))
            .isEqualTo(Location(9, 8))
    }

    companion object {

        val origin = Location(10, 10)

        @JvmStatic
        fun provideArgumentsForDistanceOfOneMove() = listOf(
            Arguments.of(Direction.North, Location(9, 10)),
            Arguments.of(Direction.NorthEast,Location(9, 11)),
            Arguments.of(Direction.East, Location(10, 11)),
            Arguments.of(Direction.SouthEast, Location(11, 11)),
            Arguments.of(Direction.South, Location(11, 10)),
            Arguments.of(Direction.SouthWest, Location(11, 9)),
            Arguments.of(Direction.West, Location(10, 9)),
            Arguments.of(Direction.NorthWest, Location(9, 9)),
        )

        @JvmStatic
        fun provideArgumentsForDistanceOfNineMove() = listOf(
            Arguments.of(Direction.North, Location(1, 10)),
            Arguments.of(Direction.NorthEast,Location(1, 19)),
            Arguments.of(Direction.East, Location(10, 19)),
            Arguments.of(Direction.SouthEast, Location(19, 19)),
            Arguments.of(Direction.South, Location(19, 10)),
            Arguments.of(Direction.SouthWest, Location(19, 1)),
            Arguments.of(Direction.West, Location(10, 1)),
            Arguments.of(Direction.NorthWest, Location(1, 1)),
        )
    }
}