package com.keyflare.exchange.impl.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.navigate
import com.keyflare.exchange.feature.debugpanel.DebugPanelArgs
import com.keyflare.exchange.feature.debugpanel.api.integration.DebugPanelNavigator
import com.keyflare.exchange.feature.debugpanel.api.integration.debugPanelComponentFactory
import com.keyflare.exchange.feature.converter.MainScreenArgs
import com.keyflare.exchange.feature.converter.api.MainScreenNavigator
import com.keyflare.exchange.feature.converter.api.mainScreenComponentFactory
import com.keyflare.exchange.feature.settings.SettingsArgs
import com.keyflare.exchange.feature.settings.api.AppThemeMode
import com.keyflare.exchange.feature.settings.api.SettingsNavigator
import com.keyflare.exchange.feature.settings.api.settingsComponentFactory
import com.keyflare.exchange.impl.di.RootDi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface RootChildConfig {

    @Serializable
    data class MainScreen(val args: MainScreenArgs) : RootChildConfig

    @Serializable
    data class DebugPanelScreen(val args: DebugPanelArgs) : RootChildConfig

    @Serializable
    data class SettingsScreen(val args: SettingsArgs) : RootChildConfig
}

internal class RootNavigationGraph(private val di: RootDi) {

    val themeMode: StateFlow<AppThemeMode> = di.appThemeStore.themeMode

    private lateinit var rootComponent: RootComponentImpl

    fun setRootComponent(rootComponent: RootComponentImpl) {
        this.rootComponent = rootComponent
    }

    fun rootChildFactory(
        config: RootChildConfig,
        componentContext: ComponentContext
    ): Any = when (config) {

        is RootChildConfig.MainScreen -> mainScreenComponentFactory(
            componentContext = componentContext,
            args = config.args,
            di = di.mainScreenDi,
            navigator = object : MainScreenNavigator {
                override fun navigateToDebugPanel() =
                    navigateTo(RootChildConfig.DebugPanelScreen(DebugPanelArgs.StartScreenArgs))

                override fun navigateToSettings(): Unit =
                    navigateTo(RootChildConfig.SettingsScreen(SettingsArgs))
            },
        )

        is RootChildConfig.DebugPanelScreen -> {
            debugPanelComponentFactory(
                componentContext = componentContext,
                args = config.args,
                navigator = object : DebugPanelNavigator {
                    override fun navigateBack() =
                        this@RootNavigationGraph.navigateBack()

                    override fun navigateToDebugPanelScreen(args: DebugPanelArgs) =
                        navigateTo(RootChildConfig.DebugPanelScreen(args))

                    override fun openNetworkLogs() {
                        di.platformDependencies.openNetworkLogs?.invoke()
                    }
                },
            )
        }

        is RootChildConfig.SettingsScreen -> settingsComponentFactory(
            componentContext = componentContext,
            args = config.args,
            di = di.settingsDi,
            navigator = object : SettingsNavigator {
                override fun navigateBack() = this@RootNavigationGraph.navigateBack()
            }
        )
    }

    private fun navigateTo(config: RootChildConfig) {
        rootComponent.navigation.navigate { it + config }
    }

    private fun navigateBack() {
        rootComponent.navigation.navigate { it.dropLast(1) }
    }
}
