package com.soberg.aoc.utlities.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger

inline fun <T> Iterable<T>.asyncCountBlocking(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    crossinline selector: (T) -> Boolean,
): Int = runBlocking(dispatcher) {
    asyncCount(selector)
}

suspend inline fun <T> Iterable<T>.asyncCount(crossinline selector: (T) -> Boolean): Int {
    val count = AtomicInteger()
    coroutineScope {
        forEach {
            launch {
                count.getAndAdd(if (selector(it)) 1 else 0)
            }
        }
    }
    return count.get()
}