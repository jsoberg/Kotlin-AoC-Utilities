package com.soberg.aoc.utlities.extensions

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@JvmName("asyncSumOfIntBlocking")
inline fun <T> Iterable<T>.asyncSumOfBlocking(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    crossinline selector: (T) -> Int,
): Int = runBlocking(dispatcher) {
    asyncSumOf(selector)
}

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@JvmName("asyncSumOfInt")
suspend inline fun <T> Iterable<T>.asyncSumOf(crossinline selector: (T) -> Int): Int {
    val sum = AtomicInteger()
    coroutineScope {
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
@JvmName("asyncSumOfLongBlocking")
inline fun <T> Iterable<T>.asyncSumOfBlocking(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    crossinline selector: (T) -> Long,
): Long = runBlocking(dispatcher){
    asyncSumOf(selector)
}

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@JvmName("asyncSumOfLong")
suspend inline fun <T> Iterable<T>.asyncSumOf(crossinline selector: (T) -> Long): Long {
    val sum = AtomicLong()
    coroutineScope {
        forEach {
            launch {
                sum.getAndAdd(selector(it))
            }
        }
    }
    return sum.get()
}
