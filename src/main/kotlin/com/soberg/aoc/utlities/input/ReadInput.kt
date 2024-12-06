package com.soberg.aoc.utlities.input

import com.soberg.kotlin.aoc.api.AdventOfCodeInputApi
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText

/**
 * Pulls and caches input for specified [year] and [day], assuming your secret token is available in session-token.secret.
 */
fun readInput(year: Int, day: Int) = AdventOfCodeInputApi(
    cachingStrategy = AdventOfCodeInputApi.CachingStrategy.LocalTextFile("input")
).blockingReadInput(
    year = year,
    day = day,
    sessionToken = readSessionToken(),
).getOrThrow()

private fun readSessionToken(): String {
    val secretTokenFile = Path("session-token.secret")
    require(secretTokenFile.exists()) {
        "session-token.secret file must exist and contain the sessionToken for Advent of Code"
    }
    return secretTokenFile.readText().trim()
}

/**
 * Pulls and caches (manually copied) test input for specified [year] and [day], assuming location /input/<year>/Day<day>-test.txt.
 */
fun readTestInput(year: Int, day: Int) =
    Path("input", "$year", "Day${"%02d".format(day)}-test.txt")
        .readText()
        .trim()
        .lines()