package com.keyflare.exchange.impl.di

import com.keyflare.exchange.api.ExchangePlatformDependencies
import com.keyflare.exchange.feature.converter.api.MainScreenDi
import com.keyflare.exchange.feature.settings.api.AppThemeStore
import com.keyflare.exchange.feature.settings.api.SettingsDi
import com.keyflare.exchange.impl.root.RootNavigationGraph
import com.keyflare.exchange.impl.network.createHttpClient
import io.ktor.client.HttpClient

internal class RootDi(
    internal val platformDependencies: ExchangePlatformDependencies,
) {
    internal val platformServices = platformDependencies.platformServices

    private val httpClient: HttpClient = createHttpClient(
        platformDependencies = platformDependencies,
    )

    internal val appThemeStore: AppThemeStore = AppThemeStore.create(
        dataStorePlatform = platformDependencies.dataStorePlatform,
    )

    internal val settingsDi: SettingsDi = SettingsDi(
        themeStore = appThemeStore,
        emailHelper = platformServices.emailHelper,
    )

    val mainScreenDi: MainScreenDi = MainScreenDi(
        buildType = platformDependencies.buildType,
    )

    val rootNavigationGraph: RootNavigationGraph = RootNavigationGraph(this)
}
