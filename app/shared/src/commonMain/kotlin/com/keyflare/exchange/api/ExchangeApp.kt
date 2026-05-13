package com.keyflare.exchange.api

import com.arkivanov.decompose.ComponentContext
import com.keyflare.common.utils.AppLogger
import com.keyflare.exchange.core.analytics.A
import com.keyflare.exchange.impl.di.RootDi
import com.keyflare.exchange.impl.root.RootComponentImpl

public class ExchangeApp(
    platformDependencies: ExchangePlatformDependencies,
) {
    private val di: RootDi

    init {
        AppLogger.initialize(platformDependencies.buildType)
        A.initialize(platformDependencies.analyticsAgent)
        di = RootDi(platformDependencies)
    }

    public fun onPlatformCreate(componentContext: ComponentContext): ExchangeRootComponent {
        val component = RootComponentImpl(
            componentContext = componentContext,
            rootNavigationGraph = di.rootNavigationGraph,
        )
        return component
    }
}
