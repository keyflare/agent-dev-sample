package com.keyflare.exchange.core.utilityscreen.internal.screen.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.ds.component.DsAppBar
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState.Screen
import com.keyflare.exchange.core.utilityscreen.internal.common.BindNode

@Composable
internal fun GeneralScreenView(
    state: Screen.General,
    onBackClick: () -> Unit,
    onUiEvent: (UtilityScreenUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (CustomTheme.colors.isDark) {
        CustomTheme.colors.background
    } else {
        CustomTheme.colors.surfaceSecondary
    }

    Column(
        modifier = modifier
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
    ) {
        DsAppBar(
            title = state.title,
            dividerVisible = true,
            background = Color.Transparent,
            onBackClick = onBackClick,
        )
        state.nodes.forEach { node ->
            BindNode(
                state = node,
                modifier = Modifier.fillMaxWidth(),
                onUiEvent = onUiEvent,
            )
        }
    }
}
