package com.keyflare.exchange.core.utilityscreen.internal.common

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.icons.compose.imageVector
import com.keyflare.exchange.core.icons.compose.list.Settings
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState

@Composable
internal fun ContentView(
    state: UtilityScreenViewState.Content,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is UtilityScreenViewState.Content.Icon -> {
            ContentIcon(state, modifier)
        }

        is UtilityScreenViewState.Content.None -> {
            /* nothing */
        }
    }
}

@Composable
private fun ContentIcon(
    state: UtilityScreenViewState.Content.Icon,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = state.icon.imageVector(default = AppIcon.Compose.Settings),
        tint = CustomTheme.colors.iconsAction,
        contentDescription = null,
        modifier = modifier,
    )
}
