package com.keyflare.common.utils

import java.util.concurrent.atomic.AtomicInteger

actual class AtomicIncrement actual constructor(initial: Int) {
    private val atomic = AtomicInteger(initial)

    actual fun incrementAndGet(): Int {
        return atomic.incrementAndGet()
    }
}
