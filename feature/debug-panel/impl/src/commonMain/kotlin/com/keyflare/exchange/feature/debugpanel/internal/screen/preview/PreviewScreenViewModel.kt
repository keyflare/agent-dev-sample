package com.keyflare.exchange.feature.debugpanel.internal.screen.preview

import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.feature.debugpanel.api.integration.DebugPanelNavigator
import com.keyflare.exchange.feature.debugpanel.api.screen.base.DebugPanelScreenBaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class PreviewScreenViewModel(
    navigator: DebugPanelNavigator,
) : DebugPanelScreenBaseViewModel(navigator) {

    override val viewState: StateFlow<UtilityScreenViewState.Screen> =
        MutableStateFlow(PreviewScreenState.toViewState())
}
