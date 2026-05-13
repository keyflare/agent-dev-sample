package com.keyflare.common.utils

import kotlin.concurrent.AtomicInt

actual class AtomicIncrement actual constructor(initial: Int) {
    private val atomic = AtomicInt(initial)

    actual fun incrementAndGet(): Int {
        return atomic.incrementAndGet()
    }
}
