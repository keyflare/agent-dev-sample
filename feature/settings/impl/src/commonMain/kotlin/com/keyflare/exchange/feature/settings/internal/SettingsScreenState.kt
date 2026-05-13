package com.keyflare.exchange.feature.settings.internal

import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.feature.settings.api.AppThemeMode

internal data object SettingsScreenState {

    private const val SCREEN_ID = "settings"
    private const val APPEARANCE_GROUP_ID = "appearance"
    private const val FEEDBACK_GROUP_ID = "feedback"

    const val LIGHT_THEME_NODE_ID = "theme_light"
    const val DARK_THEME_NODE_ID = "theme_dark"
    const val SYSTEM_THEME_NODE_ID = "theme_system"
    const val CONTACT_US_NODE_ID = "contact_us"
    const val REPORT_A_BUG_NODE_ID = "report_a_bug"

    fun toViewState(
        selected: AppThemeMode,
    ): UtilityScreenViewState.Screen.General {
        return UtilityScreenViewState.Screen.General(
            id = SCREEN_ID,
            title = "Settings",
            nodes = listOf(
                UtilityScreenViewState.ScreenNode.Group(
                    id = APPEARANCE_GROUP_ID,
                    title = "Appearance",
                    nodes = listOf(
                        themeItem(
                            id = LIGHT_THEME_NODE_ID,
                            label = "Light",
                            selected = selected == AppThemeMode.Light,
                        ),
                        themeItem(
                            id = DARK_THEME_NODE_ID,
                            label = "Dark",
                            selected = selected == AppThemeMode.Dark,
                        ),
                        themeItem(
                            id = SYSTEM_THEME_NODE_ID,
                            label = "System",
                            selected = selected == AppThemeMode.System,
                            drawLine = false,
                        ),
                    ),
                ),
                UtilityScreenViewState.ScreenNode.Group(
                    id = FEEDBACK_GROUP_ID,
                    title = "Feedback",
                    nodes = listOf(
                        infoItem(CONTACT_US_NODE_ID, "Contact Us"),
                        infoItem(REPORT_A_BUG_NODE_ID, "Report a Bug", drawLine = false),
                    ),
                ),
            ),
        )
    }

    private fun themeItem(
        id: String,
        label: String,
        selected: Boolean,
        drawLine: Boolean = true,
    ): UtilityScreenViewState.ScreenNode.Leaf.ListItem.Simple {
        return UtilityScreenViewState.ScreenNode.Leaf.ListItem.Simple(
            id = id,
            label = label,
            drawLine = drawLine,
            additional = if (selected) {
                UtilityScreenViewState.Content.Icon(AppIcon.CHECK)
            } else {
                UtilityScreenViewState.Content.None
            },
        )
    }

    private fun infoItem(
        id: String,
        label: String,
        drawLine: Boolean = true,
    ): UtilityScreenViewState.ScreenNode.Leaf.ListItem.Simple {
        return UtilityScreenViewState.ScreenNode.Leaf.ListItem.Simple(
            id = id,
            label = label,
            drawLine = drawLine,
        )
    }
}
