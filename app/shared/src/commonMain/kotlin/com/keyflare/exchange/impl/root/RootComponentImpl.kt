package com.keyflare.exchange.impl.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.value.Value
import com.keyflare.exchange.api.ExchangeRootComponent
import com.keyflare.exchange.feature.converter.MainScreenArgs
import com.keyflare.exchange.feature.settings.api.AppThemeMode
import kotlinx.coroutines.flow.StateFlow

internal class RootComponentImpl(
    componentContext: ComponentContext,
    rootNavigationGraph: RootNavigationGraph,
) : ExchangeRootComponent, ComponentContext by componentContext {

    val navigation = StackNavigation<RootChildConfig>()

    init {
        rootNavigationGraph.setRootComponent(this)
    }

    override val stack: Value<ChildStack<*, Any>> = childStack(
        source = navigation,
        serializer = RootChildConfig.serializer(),
        initialConfiguration = RootChildConfig.MainScreen(MainScreenArgs),
        key = MAIN_STACK_KEY,
        handleBackButton = true,
        childFactory = rootNavigationGraph::rootChildFactory,
    )

    override val themeMode: StateFlow<AppThemeMode> = rootNavigationGraph.themeMode

    override fun onBack(toIndex: Int) {
        navigation.navigate { it.take(toIndex + 1) }
    }

    companion object {
        const val MAIN_STACK_KEY = "MainStack"
    }
}
