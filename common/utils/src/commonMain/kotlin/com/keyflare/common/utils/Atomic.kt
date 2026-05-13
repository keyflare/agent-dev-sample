package com.keyflare.common.utils

expect class AtomicIncrement(initial: Int) {
    fun incrementAndGet(): Int
}
