package com.keyflare.exchange.core.utilityscreen.internal.screen.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.ds.component.DsAppBar
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState

@Composable
internal fun CustomScreenView(
    state: UtilityScreenViewState.Screen.Custom,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val backgroundColor = if (isSystemInDarkTheme()) {
        CustomTheme.colors.background
    } else {
        CustomTheme.colors.surfaceSecondary
    }

    Column(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxSize(),
    ) {
        DsAppBar(
            title = state.title,
            dividerVisible = true,
            background = Color.Transparent,
            onBackClick = onBackClick,
        )
        content()
    }
}
