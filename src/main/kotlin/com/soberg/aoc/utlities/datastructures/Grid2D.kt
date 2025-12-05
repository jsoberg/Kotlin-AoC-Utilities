package com.soberg.aoc.utlities.datastructures

import kotlin.experimental.ExperimentalTypeInference

/** A uniform 2D grid with elements of type [T]. */
data class Grid2D<T>(
    private val grid: List<List<T>>,
) {
    init {
        val colSize = grid[0].size
        for (i in 1..<grid.size) {
            if (colSize != grid[i].size) {
                error("Grid is not uniform - row at index $i has a differing # of columns")
            }
        }
    }

    val rowSize: Int
        get() = grid.size

    val colSize: Int
        get() = grid[0].size

    // region Grid elements

    infix operator fun get(location: Location): T = grid[location.row][location.col]

    operator fun get(row: Int, col: Int): T = grid[row][col]

    infix operator fun contains(location: Location): Boolean = isInBounds(location)

    /** @return true if the specified [location] is in the bounds of this grid, false if not. */
    fun isInBounds(location: Location): Boolean = isInBounds(location.row, location.col)

    /** @return true if the specified [row], [col] location is in the bounds of this grid, false if not. */
    fun isInBounds(row: Int, col: Int): Boolean =
        (row <= grid.lastIndex && row >= 0) && (col <= grid[0].lastIndex && col >= 0)

    /** @return The element for the specified element starting at [from] and moving in [direction] for
     *  [distance], null if this element doesn't exist or location would be out of bounds of the grid. */
    fun elementAt(
        from: Location,
        direction: Direction,
        distance: Int,
    ): T? {
        val endLocation = from.move(direction, distance)
        return if (isInBounds(endLocation)) {
            get(endLocation)
        } else null
    }

    /** @return The collection of elements (as a [List]) that exist from the start [from] (inclusive)
     * in [direction] for [numElementsToCollect], null if any location would be out of bounds of this grid.
     *
     * Note that [numElementsToCollect] is inclusive of the starting location - therefore, a value
     * of 1 would result in a single item list of just the element at [from]. */
    fun collect(
        from: Location,
        direction: Direction,
        numElementsToCollect: Int,
    ): List<T>? {
        val finalLocation = from.move(direction, numElementsToCollect - 1)
        if (!isInBounds(finalLocation)) {
            return null
        }
        return buildList {
            for (i in 0..<numElementsToCollect) {
                add(get(from.move(direction, i)))
            }
        }
    }

    /** @return Map of elements to list of their respective locations. */
    fun elementToLocationsMap(): Map<T, List<Location>> =
        filterElementToLocationsMap(filter = { _, _ -> true })

    /** @return Map of elements and their respective locations in the grid that pass [filter]. */
    fun filterElementToLocationsMap(
        filter: (element: T, location: Location) -> Boolean
    ): Map<T, List<Location>> {
        val elementToLocationsMap = hashMapOf<T, List<Location>>()
        traverse { at ->
            val element = get(at)
            if (filter(element, at)) {
                val currentLocations = elementToLocationsMap[element] ?: emptyList()
                elementToLocationsMap[element] = currentLocations + at
            }
        }
        return elementToLocationsMap
    }

    /** Returns all neighboring Locations for this Location, in the specified [directions].
     * If any neighbor location is out of bounds for this grid, it will not be included. */
    fun neighbors(location: Location, directions: Collection<Direction>): Set<Location> = buildSet {
        directions.forEach { direction ->
            val moved = location.move(direction)
            if (moved in this@Grid2D) {
                add(moved)
            }
        }
    }

    // endregion

    // region Grid traversal

    /** Walks through each location in this grid, with a callback for each location. */
    inline fun traverse(at: (location: Location) -> Unit) {
        for (row in 0..<rowSize) {
            for (col in 0..<colSize) {
                at(Location(row, col))
            }
        }
    }

    /** Walks through each location in this grid, with a callback for each location and corresponding element. */
    inline fun traverse(at: (location: Location, element: T) -> Unit) {
        traverse { location ->
            at(location, get(location))
        }
    }

    /** Traverses each location starting from [from] (inclusive) in [direction], until reaching the bounds of the grid. */
    inline fun traverseDirection(
        from: Location,
        direction: Direction,
        at: (location: Location) -> Unit,
    ) {
        var current = from
        while (isInBounds(current)) {
            at(current)
            current = current.move(direction)
        }
    }

    /** Traverses each location in the grid, counting the true results of [at]. */
    inline fun count(at: (location: Location, element: T) -> Boolean): Int {
        var total = 0
        traverse { location ->
            if (at(location, get(location))) {
                total++
            }
        }
        return total
    }

    /** Traverses each location in the grid, summing the result of [at]. */
    @OptIn(ExperimentalTypeInference::class)
    @OverloadResolutionByLambdaReturnType
    @JvmName("sumOfInt")
    inline fun sumOf(at: (location: Location, element: T) -> Int): Int {
        var sum = 0
        traverse { location ->
            sum += at(location, get(location))
        }
        return sum
    }

    /** Traverses each location in the grid, summing the result of [at]. */
    @OptIn(ExperimentalTypeInference::class)
    @OverloadResolutionByLambdaReturnType
    @JvmName("sumOfLong")
    inline fun sumOf(at: (location: Location, element: T) -> Long): Long {
        var sum = 0L
        traverse { location ->
            sum += at(location, get(location))
        }
        return sum
    }

    // endregion

    // region Grid modification

    /** @return A new copy of this [Grid2D] with the specified [element] replaced at [location].
     * @throws [IllegalArgumentException] if [location] not in bounds for this grid. */
    fun replace(location: Location, element: T): Grid2D<T> {
        require(isInBounds(location)) {
            "$location not in bounds for this grid"
        }
        val replacedRow: MutableList<T> = grid[location.row].toMutableList()
        replacedRow[location.col] = element
        return copy(
            grid = grid.toMutableList().apply {
                set(location.row, replacedRow)
            }
        )
    }

    /** @return A new copy of this [Grid2D] with the underlying grid (in 2D list format) returned from [block]. */
    fun modify(block: (mutableGrid: MutableList<MutableList<T>>) -> Unit): Grid2D<T> {
        val mutableGrid = grid.map { it.toMutableList() }.toMutableList()
        block(mutableGrid)
        return mutableGrid.toGrid2D()
    }

    // endregion

    // Grid string representation/printing

    /** Prints the contents of this grid to console in row/col format. */
    inline fun toString(
        toPrint: (Location) -> String,
    ): String = StringBuilder().apply {
        traverse { location ->
            append(toPrint(location))
            if (location.col == (colSize - 1)) {
                appendLine()
            }
        }
    }.toString()

    override fun toString() = toString { get(it).toString() }

    /** Prints this Grid representation to console. */
    fun print(
        toPrint: (Location) -> String = { get(it).toString() },
    ) = print(toString(toPrint))

    // endregion

    // region Grid location/distance

    data class Location(
        val row: Int,
        val col: Int,
    ) {
        fun move(direction: Direction, distance: Int = 1) = Location(
            row = row + (direction.row.integerMovement * distance),
            col = col + (direction.col.integerMovement * distance),
        )

        infix operator fun plus(other: Distance): Location =
            Location(row = this.row + other.rowDelta, col = this.col + other.colDelta)

        infix operator fun minus(other: Distance): Location =
            Location(row = this.row - other.rowDelta, col = this.col - other.colDelta)

        infix fun distanceTo(other: Location): Distance =
            Distance(
                rowDelta = this.row - other.row,
                colDelta = this.col - other.col,
            )

        override fun toString(): String = "{Row: $row, Col: $col}"

        data class Distance(
            val rowDelta: Int,
            val colDelta: Int,
        )

        companion object {
            infix fun Int.loc(col: Int): Location = Location(row = this, col = col)
        }
    }

    // endregion

    // region Grid cardinal directions

    enum class Direction(
        internal val row: Movement = Movement.None,
        internal val col: Movement = Movement.None,
    ) {
        North(row = Movement.Negative),
        NorthEast(row = Movement.Negative, col = Movement.Positive),
        East(col = Movement.Positive),
        SouthEast(row = Movement.Positive, col = Movement.Positive),
        South(row = Movement.Positive),
        SouthWest(row = Movement.Positive, col = Movement.Negative),
        West(col = Movement.Negative),
        NorthWest(row = Movement.Negative, col = Movement.Negative);

        internal enum class Movement(
            val integerMovement: Int,
        ) {
            Positive(1),
            Negative(-1),
            None(0),
        }

        val opposite
            get() = when (this) {
                North -> South
                NorthEast -> SouthWest
                East -> West
                SouthEast -> NorthWest
                South -> North
                SouthWest -> NorthEast
                West -> East
                NorthWest -> SouthEast
            }

        companion object {
            /** The four main cardinal directions (North, South, East, West). */
            val MainDirections = setOf(North, South, East, West)
        }
    }

    // endregion
}

// region Grid builder extensions

fun <T> List<List<T>>.toGrid2D() = Grid2D(this)

/** Converts the common AoC input (List of lines (as String) from input file). */
inline fun <T> List<String>.toGrid2D(convertLine: (String) -> List<T>) = map { line ->
    convertLine(line)
}.toGrid2D()

fun List<String>.toCharGrid2D() = toGrid2D { line -> line.toCharArray().asList() }

// endregion