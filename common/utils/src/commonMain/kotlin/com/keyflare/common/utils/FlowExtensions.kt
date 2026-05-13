package com.keyflare.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

inline fun <T, R> StateFlow<T>.mapState(
    scope: CoroutineScope = MainScope(),
    crossinline block: (value: T) -> R,
): StateFlow<R> = map { block(it) }.stateIn(scope, SharingStarted.Eagerly, block(this.value))
