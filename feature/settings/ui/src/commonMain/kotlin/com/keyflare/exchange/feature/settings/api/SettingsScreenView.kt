package com.keyflare.exchange.feature.settings.api

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenView

@Composable
public fun SettingsScreenView(viewModel: SettingsViewModel) {
    val viewState = viewModel.viewState.collectAsState()

    UtilityScreenView(
        state = viewState.value,
        onUiEvent = viewModel::onUiEvent,
        modifier = Modifier.fillMaxSize(),
    )
}
