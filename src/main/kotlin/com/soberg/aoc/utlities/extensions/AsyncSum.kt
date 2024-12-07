package com.soberg.aoc.utlities.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@JvmName("asyncSumOfInt")
inline fun <T> Iterable<T>.asyncSumOf(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    crossinline selector: (T) -> Int,
): Int {
    val sum = AtomicInteger()
    runBlocking(dispatcher) {
        forEach {
            launch {
                sum.getAndAdd(selector(it))
            }
        }
    }
    return sum.get()
}

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@JvmName("asyncSumOfLong")
inline fun <T> Iterable<T>.asyncSumOf(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    crossinline selector: (T) -> Long,
): Long {
    val sum = AtomicLong()
    runBlocking(dispatcher) {
        forEach {
            launch {
                sum.getAndAdd(selector(it))
            }
        }
    }
    return sum.get()
}
