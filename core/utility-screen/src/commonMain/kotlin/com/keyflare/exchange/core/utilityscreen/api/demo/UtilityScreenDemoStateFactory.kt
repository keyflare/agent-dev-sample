package com.keyflare.exchange.core.utilityscreen.api.demo

import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState.Content
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState.Screen
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState.ScreenNode

public object UtilityScreenDemoStateFactory {

    public fun createGeneralScreen(): Screen.General {
        return Screen.General(
            id = "utility-demo",
            title = "Utility Screen Demo",
            nodes = listOf(
                ScreenNode.Group(
                    id = "actions",
                    title = "Actions",
                    nodes = listOf(
                        ScreenNode.Leaf.ListItem.Simple(
                            id = "details",
                            label = "Open details",
                            additional = Content.Icon(AppIcon.CHEVRON_RIGHT),
                        ),
                        ScreenNode.Leaf.ListItem.Simple(
                            id = "about",
                            label = "About",
                            additional = Content.Icon(AppIcon.EXCHANGE),
                            drawLine = false,
                        ),
                    ),
                ),
                ScreenNode.Group(
                    id = "preferences",
                    title = "Preferences",
                    nodes = listOf(
                        ScreenNode.Leaf.ListItem.Switcher(
                            id = "enabled",
                            label = "Enabled",
                            value = true,
                            onValueChange = {},
                        ),
                        ScreenNode.Leaf.ListItem.Input.Text(
                            id = "notes",
                            label = "Notes",
                            initialValue = "Demo value",
                            allowCopy = true,
                            onValueChange = {},
                            drawLine = false,
                        ),
                    ),
                ),
            ),
        )
    }
}
