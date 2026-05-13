package com.keyflare.exchange.feature.debugpanel.internal.screen.base

import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState

internal interface ScreenBaseState {

    fun toViewState(
        onUiEvent: (UtilityScreenUiEvent) -> Unit,
    ): UtilityScreenViewState.Screen
}
