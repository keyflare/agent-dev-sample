package com.keyflare.exchange.core.utilityscreen.internal.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.ds.component.DsCard
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.core.utilityscreen.internal.common.BindNode

@Composable
internal fun GroupView(
    state: UtilityScreenViewState.ScreenNode.Group,
    modifier: Modifier = Modifier,
    onUiEvent: (UtilityScreenUiEvent) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = state.title.uppercase(),
            style = CustomTheme.type.body3,
            color = CustomTheme.colors.textSecondary,
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 4.dp,
                start = 32.dp,
            )
        )
        DsCard(
            cornerRadius = 10.dp,
            shadow = false,
            border = false,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Column {
                state.nodes.forEach {
                    BindNode(
                        state = it,
                        modifier = Modifier.fillMaxWidth(),
                        onUiEvent = onUiEvent,
                    )
                }
            }
        }
    }
}
