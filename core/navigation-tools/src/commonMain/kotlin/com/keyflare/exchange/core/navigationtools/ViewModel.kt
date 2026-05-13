package com.keyflare.exchange.core.navigationtools

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlin.coroutines.EmptyCoroutineContext

abstract class ViewModel<ViewState: Any, UiEvent: Any> : InstanceKeeper.Instance {
    private val _context = MutableStateFlow(null as ComponentContext?)
    protected val context: StateFlow<ComponentContext?> = _context.asStateFlow()

    val viewModelScope: CoroutineScope by lazy { createViewModelScope() }

    abstract val viewState: StateFlow<ViewState>

    protected open fun onCleared() = Unit

    protected fun withComponentContext(body: suspend (ComponentContext) -> Unit) {
        context
            .filterNotNull()
            .take(1)
            .onEach(body)
            .launchIn(viewModelScope)
    }

    internal fun updateContext(context: ComponentContext) {
        _context.value = context
    }

    open fun onUiEvent(event: UiEvent) = Unit

    final override fun onDestroy() {
        onCleared()
        viewModelScope.cancel()
    }

    open class Empty : ViewModel<Unit, Unit>() {
        final override val viewState: StateFlow<Unit> = MutableStateFlow(Unit)
    }
}

private fun createViewModelScope(): CoroutineScope {
    val dispatcher = try {
        // In platforms where `Dispatchers.Main` is not available, Kotlin Multiplatform will throw
        // an exception (the specific exception type may depend on the platform). Since there's no
        // direct functional alternative, we use `EmptyCoroutineContext` to ensure that a coroutine
        // launched within this scope will run in the same context as the caller.
        Dispatchers.Main.immediate
    } finally {
        EmptyCoroutineContext
    }
    return CoroutineScope(context = dispatcher + SupervisorJob())
}
