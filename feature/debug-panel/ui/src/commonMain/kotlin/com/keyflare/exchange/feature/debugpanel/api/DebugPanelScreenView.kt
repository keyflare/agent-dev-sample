package com.keyflare.exchange.feature.debugpanel.api

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenView
import com.keyflare.exchange.feature.debugpanel.api.screen.base.DebugPanelScreenBaseViewModel

@Composable
public fun DebugPanelScreenView(viewModel: DebugPanelScreenBaseViewModel) {
    val viewState = viewModel.viewState.collectAsState()

    UtilityScreenView(
        state = viewState.value,
        onUiEvent = viewModel::onUiEvent,
        modifier = Modifier.fillMaxSize(),
        customContent = { state -> CustomDebugPanelScreenRenderer(state) },
    )
}
