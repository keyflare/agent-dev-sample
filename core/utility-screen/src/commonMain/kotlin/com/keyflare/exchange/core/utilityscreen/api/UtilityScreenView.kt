package com.keyflare.exchange.core.utilityscreen.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keyflare.exchange.core.utilityscreen.internal.BindScreenView

@Composable
public fun UtilityScreenView(
    state: UtilityScreenViewState.Screen,
    onUiEvent: (UtilityScreenUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    customContent: @Composable (state: UtilityScreenViewState.Screen.Custom) -> Unit = {},
) {
    BindScreenView(
        state = state,
        onUiEvent = onUiEvent,
        modifier = modifier,
        customContent = customContent,
    )
}
