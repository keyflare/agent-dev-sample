package com.keyflare.exchange.android

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.keyflare.exchange.api.ExchangeRootComponent
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.ds.theme.ExchangeTheme
import com.keyflare.exchange.core.navigationtools.ViewModelComponent
import com.keyflare.exchange.feature.debugpanel.api.DebugPanelScreenView
import com.keyflare.exchange.feature.debugpanel.api.screen.base.DebugPanelScreenBaseViewModel
import com.keyflare.exchange.feature.converter.api.MainScreenView
import com.keyflare.exchange.feature.converter.api.MainScreenViewModel
import com.keyflare.exchange.feature.settings.api.AppThemeMode
import com.keyflare.exchange.feature.settings.api.SettingsScreenView
import com.keyflare.exchange.feature.settings.api.SettingsViewModel

@Composable
fun ExchangeAppView(component: ExchangeRootComponent) {
    val themeMode = component.themeMode.collectAsState()
    val darkTheme = when (themeMode.value) {
        AppThemeMode.System -> isSystemInDarkTheme()
        AppThemeMode.Light -> false
        AppThemeMode.Dark -> true
    }

    ExchangeTheme(darkTheme = darkTheme) {
        Children(
            stack = component.stack,
            animation = stackAnimation(fade()),
            modifier = Modifier
                .fillMaxSize()
                .background(color = CustomTheme.colors.background)
        ) { child ->
            val c = child.instance as ViewModelComponent<*>

            when (val vm = c.viewModel) {
                is MainScreenViewModel -> MainScreenView(vm)
                is DebugPanelScreenBaseViewModel -> DebugPanelScreenView(vm)
                is SettingsViewModel -> SettingsScreenView(vm)
            }
        }
    }
}
