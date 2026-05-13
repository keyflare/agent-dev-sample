package com.keyflare.exchange.feature.settings.api

import com.keyflare.common.utils.mapState
import com.keyflare.exchange.core.navigationtools.ViewModel
import com.keyflare.exchange.core.platform.RatebenchEmailConfig
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.feature.settings.SettingsArgs
import com.keyflare.exchange.feature.settings.internal.SettingsScreenState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

public class SettingsViewModel(
    @Suppress("UNUSED_PARAMETER")
    args: SettingsArgs = SettingsArgs,
    private val navigator: SettingsNavigator,
    private val di: SettingsDi,
) : ViewModel<UtilityScreenViewState.Screen, UtilityScreenUiEvent>() {

    override val viewState: StateFlow<UtilityScreenViewState.Screen> =
        di.themeStore.themeMode.mapState(viewModelScope) { selectedTheme ->
            SettingsScreenState.toViewState(selectedTheme)
        }

    override fun onUiEvent(event: UtilityScreenUiEvent) {
        when (event) {
            UtilityScreenUiEvent.OnBack -> navigator.navigateBack()
            is UtilityScreenUiEvent.OnNodeClick -> onNodeClick(event.nodeId)
        }
    }

    private fun onNodeClick(nodeId: String) {
        when (nodeId) {
            SettingsScreenState.CONTACT_US_NODE_ID -> {
                di.emailHelper.writeEmail(
                    contactEmail = RatebenchEmailConfig.CONTACT_EMAIL,
                    subject = RatebenchEmailConfig.FEEDBACK_SUBJECT,
                    body = null,
                )
                return
            }
            SettingsScreenState.REPORT_A_BUG_NODE_ID -> {
                di.emailHelper.writeEmail(
                    contactEmail = RatebenchEmailConfig.CONTACT_EMAIL,
                    subject = RatebenchEmailConfig.BUG_REPORT_SUBJECT,
                    body = null,
                )
                return
            }
        }

        val selectedTheme = when (nodeId) {
            SettingsScreenState.LIGHT_THEME_NODE_ID -> AppThemeMode.Light
            SettingsScreenState.DARK_THEME_NODE_ID -> AppThemeMode.Dark
            SettingsScreenState.SYSTEM_THEME_NODE_ID -> AppThemeMode.System
            else -> null
        } ?: return

        if (di.themeStore.themeMode.value == selectedTheme) return

        viewModelScope.launch(di.ioDispatcher) {
            di.themeStore.setThemeMode(selectedTheme)
        }
    }
}
