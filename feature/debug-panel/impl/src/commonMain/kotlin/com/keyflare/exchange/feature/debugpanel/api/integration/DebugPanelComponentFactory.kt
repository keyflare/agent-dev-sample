package com.keyflare.exchange.feature.debugpanel.api.integration

import com.arkivanov.decompose.ComponentContext
import com.keyflare.exchange.core.navigationtools.ViewModelComponent
import com.keyflare.exchange.feature.debugpanel.DebugPanelArgs
import com.keyflare.exchange.feature.debugpanel.internal.screen.preview.previewScreenComponentFactory
import com.keyflare.exchange.feature.debugpanel.internal.screen.start.startScreenComponentFactory

public fun debugPanelComponentFactory(
    componentContext: ComponentContext,
    navigator: DebugPanelNavigator,
    args: DebugPanelArgs,
): ViewModelComponent<*> {
    return when (args) {

        is DebugPanelArgs.StartScreenArgs ->
            startScreenComponentFactory(componentContext, navigator)

        is DebugPanelArgs.PreviewScreenArgs ->
            previewScreenComponentFactory(componentContext, navigator)
    }
}
