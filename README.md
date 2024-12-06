# Kotlin Advent of Code Utilities :christmas_tree:

[![](https://jitpack.io/v/jsoberg/Kotlin-AoC-Utilities.svg)](https://jitpack.io/#jsoberg/Kotlin-AoC-Utilities) [![Tests](https://github.com/jsoberg/Kotlin-AoC-Utilities/actions/workflows/run-unit-tests.yml/badge.svg)](https://github.com/jsoberg/Kotlin-AoC-Utilities/actions/workflows/run-unit-tests.yml)

Common utilities that I end up using every year for [Advent of Code](https://adventofcode.com/). Includes the [Kotlin AoC API](https://github.com/jsoberg/Kotlin-AoC-API) for automatically pulling and caching input for each day of the advent.

## Setup

This can be added to a [Kotlin Advent of Code template repository](https://github.com/kotlin-hands-on/advent-of-code-kotlin-template) or any other Kotlin JVM project through [Jitpack](https://jitpack.io/#jsoberg/Kotlin-AoC-Utilities), in your `build.gradle.kts`:

```kts
dependencies {
    implementation("com.github.jsoberg:Kotlin-AoC-Utilities:2024.1")
}
```

## Utilities

Listing of the current utilities for common functions found in Advent of Code puzzles.

### Data Structures

- [Grid2D](src/main/com/soberg/aoc/utlities/datastructures/Grid2D.kt) - Simple uniform 2D Grid with utility functions for moving in cardinal directions.


### Input Reading

- [ReadInput](src/main/com/soberg/aoc/utlities/input/ReadInput.kt) - Uses the [Kotlin AoC API](https://github.com/jsoberg/Kotlin-AoC-API) to read daily input from API, caching locally in `input/<year>/Day<day>.txt`.