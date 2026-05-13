package com.keyflare.exchange.feature.debugpanel.internal.screen.start

import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState.Content
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState.ScreenNode
import com.keyflare.exchange.feature.debugpanel.internal.screen.base.ScreenBaseState

internal data class StartScreenState(
    val showUiGroup: Boolean,
) : ScreenBaseState {

    internal companion object {
        private const val SCREEN_ID = "start"
        const val PREVIEW_NODE_ID = "preview"
    }

    override fun toViewState(
        onUiEvent: (UtilityScreenUiEvent) -> Unit,
    ): UtilityScreenViewState.Screen {
        val groups = buildList {
            if (showUiGroup) {
                add(
                    ScreenNode.Group(
                        id = "ui",
                        title = "UI",
                        nodes = listOf(
                            ScreenNode.Leaf.ListItem.Simple(
                                id = PREVIEW_NODE_ID,
                                label = "Icons Preview",
                                additional = Content.Icon(AppIcon.CHEVRON_RIGHT),
                                drawLine = false,
                            ),
                        ),
                    )
                )
            }
        }

        return UtilityScreenViewState.Screen.General(
            id = SCREEN_ID,
            title = "Debug Panel",
            nodes = groups,
        )
    }
}
