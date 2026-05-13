package com.keyflare.exchange

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

public class FlowWrapper<T : Any>(private val stateFlow: StateFlow<T>) {
    private val scope = MainScope()
    private var job: Job? = null

    public val value: T get() = stateFlow.value

    public fun observe(
        onEach: (T) -> Unit,
        onComplete: (cause: Throwable?) -> Unit,
    ) {
        job = stateFlow
            .onEach(onEach)
            .onCompletion { onComplete(it) }
            .launchIn(scope)
    }

    public fun cancel() {
        job?.cancel()
    }
}
