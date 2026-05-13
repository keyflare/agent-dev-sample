package com.keyflare.exchange.feature.debugpanel.api.screen.base

import com.keyflare.exchange.core.navigationtools.ViewModel
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.feature.debugpanel.api.integration.DebugPanelNavigator

public abstract class DebugPanelScreenBaseViewModel(
    protected val navigator: DebugPanelNavigator,
) : ViewModel<UtilityScreenViewState.Screen, UtilityScreenUiEvent>() {

    public val title: String get() = viewState.value.title

    override fun onUiEvent(event: UtilityScreenUiEvent) {
        if (handleUiEvent(event)) return

        when (event) {
            UtilityScreenUiEvent.OnBack -> {
                navigator.navigateBack()
            }
        }
    }

    protected open fun handleUiEvent(event: UtilityScreenUiEvent): Boolean = false
}
