package com.keyflare.exchange.feature.converter.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.keyflare.exchange.feature.converter.internal.MainScreenPureView

@Composable
public fun MainScreenView(viewModel: MainScreenViewModel) {
    MainScreenPureView(
        viewModel.viewState.collectAsState(),
        onUiEvent = viewModel::onUiEvent,
        onSettingsClick = { viewModel.onUiEvent(MainScreenUiEvent.OnSettingsClick) },
        onDebugPanelClick = { viewModel.onUiEvent(MainScreenUiEvent.OnDebugPanelClick) },
    )
}
