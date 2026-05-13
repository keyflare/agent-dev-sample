package com.keyflare.exchange.core.utilityscreen.internal.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.core.utilityscreen.internal.component.GroupView
import com.keyflare.exchange.core.utilityscreen.internal.component.InputListItemView
import com.keyflare.exchange.core.utilityscreen.internal.component.SimpleListItemView
import com.keyflare.exchange.core.utilityscreen.internal.component.SwitcherListItemView

@Composable
internal fun BindNode(
    state: UtilityScreenViewState.ScreenNode,
    modifier: Modifier = Modifier,
    onUiEvent: (UtilityScreenUiEvent) -> Unit,
) {
    when (state) {
        is UtilityScreenViewState.ScreenNode.Leaf.ListItem.Simple -> {
            SimpleListItemView(state, onUiEvent, modifier)
        }

        is UtilityScreenViewState.ScreenNode.Leaf.ListItem.Input -> {
            InputListItemView(state, modifier)
        }

        is UtilityScreenViewState.ScreenNode.Leaf.ListItem.Switcher -> {
            SwitcherListItemView(state, modifier)
        }

        is UtilityScreenViewState.ScreenNode.Group -> {
            GroupView(state, modifier, onUiEvent)
        }

        else -> Unit
    }
}
