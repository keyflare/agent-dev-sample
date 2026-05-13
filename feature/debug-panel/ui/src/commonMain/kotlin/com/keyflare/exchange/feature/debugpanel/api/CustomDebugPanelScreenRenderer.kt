package com.keyflare.exchange.feature.debugpanel.api

import androidx.compose.runtime.Composable
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState

@Composable
internal expect fun CustomDebugPanelScreenRenderer(
    state: UtilityScreenViewState.Screen.Custom,
)
