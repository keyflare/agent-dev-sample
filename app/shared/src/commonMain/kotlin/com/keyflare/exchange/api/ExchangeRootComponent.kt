package com.keyflare.exchange.api

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.keyflare.exchange.feature.settings.api.AppThemeMode
import kotlinx.coroutines.flow.StateFlow

public interface ExchangeRootComponent {
    public val stack: Value<ChildStack<*, Any>>
    public val themeMode: StateFlow<AppThemeMode>
    public fun onBack(toIndex: Int)
}
