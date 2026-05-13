package com.keyflare.exchange.feature.debugpanel.internal.screen.start

import com.keyflare.common.utils.Platform
import com.keyflare.common.utils.mapState
import com.keyflare.common.utils.platform
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.feature.debugpanel.DebugPanelArgs
import com.keyflare.exchange.feature.debugpanel.api.integration.DebugPanelNavigator
import com.keyflare.exchange.feature.debugpanel.api.screen.base.DebugPanelScreenBaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class StartScreenViewModel(
    navigator: DebugPanelNavigator,
) : DebugPanelScreenBaseViewModel(navigator) {

    private val state = MutableStateFlow(
        StartScreenState(showUiGroup = platform == Platform.ANDROID),
    )

    override val viewState: StateFlow<UtilityScreenViewState.Screen> =
        state.mapState(viewModelScope) { it.toViewState(::onUiEvent) }

    override fun handleUiEvent(event: UtilityScreenUiEvent): Boolean {
        return when (event) {

            is UtilityScreenUiEvent.OnNodeClick -> {
                when (event.nodeId) {

                    StartScreenState.PREVIEW_NODE_ID -> {
                        navigator.navigateToDebugPanelScreen(DebugPanelArgs.PreviewScreenArgs)
                        true
                    }

                    else -> false
                }
            }

            else -> false
        }
    }
}
