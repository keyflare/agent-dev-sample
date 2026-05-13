package com.keyflare.exchange.core.utilityscreen.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.core.utilityscreen.internal.screen.custom.CustomScreenView
import com.keyflare.exchange.core.utilityscreen.internal.screen.general.GeneralScreenView

@Composable
internal fun BindScreenView(
    state: UtilityScreenViewState.Screen,
    onUiEvent: (UtilityScreenUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    customContent: @Composable (state: UtilityScreenViewState.Screen.Custom) -> Unit = {},
) {
    when (state) {
        is UtilityScreenViewState.Screen.General -> {
            GeneralScreenView(
                state = state,
                onBackClick = { onUiEvent(UtilityScreenUiEvent.OnBack) },
                onUiEvent = onUiEvent,
                modifier = modifier,
            )
        }

        is UtilityScreenViewState.Screen.Custom -> {
            CustomScreenView(
                state = state,
                onBackClick = { onUiEvent(UtilityScreenUiEvent.OnBack) },
                modifier = modifier,
            ) {
                customContent(state)
            }
        }
    }
}
