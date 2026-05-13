package com.keyflare.exchange.core.utilityscreen.api.demo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenView
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent

@Composable
public fun UtilityScreenDemoView(
    modifier: Modifier = Modifier,
    onUiEvent: (UtilityScreenUiEvent) -> Unit = {},
) {
    UtilityScreenView(
        state = UtilityScreenDemoStateFactory.createGeneralScreen(),
        onUiEvent = onUiEvent,
        modifier = modifier,
    )
}
