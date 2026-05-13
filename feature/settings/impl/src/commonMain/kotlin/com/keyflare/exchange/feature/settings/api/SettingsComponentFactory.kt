package com.keyflare.exchange.feature.settings.api

import com.arkivanov.decompose.ComponentContext
import com.keyflare.exchange.core.navigationtools.ViewModelComponent
import com.keyflare.exchange.feature.settings.SettingsArgs

public fun settingsComponentFactory(
    componentContext: ComponentContext,
    args: SettingsArgs,
    di: SettingsDi,
    navigator: SettingsNavigator,
): ViewModelComponent<SettingsViewModel> = ViewModelComponent.with(componentContext) {
    SettingsViewModel(
        args = args,
        navigator = navigator,
        di = di,
    )
}
