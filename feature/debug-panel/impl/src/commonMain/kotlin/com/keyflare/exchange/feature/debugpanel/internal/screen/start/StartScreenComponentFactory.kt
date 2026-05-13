package com.keyflare.exchange.feature.debugpanel.internal.screen.start

import com.arkivanov.decompose.ComponentContext
import com.keyflare.exchange.core.navigationtools.ViewModelComponent
import com.keyflare.exchange.feature.debugpanel.api.integration.DebugPanelNavigator
import com.keyflare.exchange.feature.debugpanel.api.screen.base.DebugPanelScreenBaseViewModel

internal fun startScreenComponentFactory(
    componentContext: ComponentContext,
    navigator: DebugPanelNavigator,
): ViewModelComponent<DebugPanelScreenBaseViewModel> = ViewModelComponent.with(componentContext) {
    StartScreenViewModel(navigator)
}
