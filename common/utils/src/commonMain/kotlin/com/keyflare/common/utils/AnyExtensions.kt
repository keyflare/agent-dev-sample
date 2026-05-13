package com.keyflare.common.utils

inline fun <reified T> Any?.cast(): T = this as T
inline fun <reified T> Any?.castOrNull(): T? = this as? T

inline fun <T> T?.alsoIfNull(block: () -> Unit) = this.also { if (it == null) block() }
inline fun <T> T?.letIfNull(block: () -> T): T = this ?: block()
