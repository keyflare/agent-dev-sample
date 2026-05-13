package com.keyflare.exchange.feature.converter.api

import com.arkivanov.decompose.ComponentContext
import com.keyflare.exchange.core.navigationtools.ViewModelComponent
import com.keyflare.exchange.feature.converter.MainScreenArgs

public fun mainScreenComponentFactory(
    componentContext: ComponentContext,
    args: MainScreenArgs,
    di: MainScreenDi,
    navigator: MainScreenNavigator,
): ViewModelComponent<MainScreenViewModel> = ViewModelComponent.with(componentContext) {
    MainScreenViewModel(args, navigator, di)
}
