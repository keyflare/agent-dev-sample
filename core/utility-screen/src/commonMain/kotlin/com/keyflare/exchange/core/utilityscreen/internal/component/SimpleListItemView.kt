package com.keyflare.exchange.core.utilityscreen.internal.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState.ScreenNode.Leaf.ListItem
import com.keyflare.exchange.core.utilityscreen.internal.common.ContentView

@Composable
internal fun SimpleListItemView(
    state: ListItem.Simple,
    onClick: (UtilityScreenUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clickable(onClick = { onClick(state.onClick) })
                .height(48.dp)
                .padding(
                    start = 16.dp,
                    end = 8.dp,
                    top = 12.dp,
                    bottom = 12.dp,
                ),
        ) {
            Text(
                text = state.label,
                style = CustomTheme.type.body1,
                color = CustomTheme.colors.textPrimary,
                modifier = Modifier.weight(1f),
            )
            ContentView(
                state = state.additional,
                modifier = Modifier.size(24.dp),
            )
        }
        if (state.drawLine) {
            Divider(
                color = CustomTheme.colors.divider,
                startIndent = 16.dp,
                modifier = Modifier
                    .height(0.5.dp)
                    .align(Alignment.BottomStart),
            )
        }
    }
}
