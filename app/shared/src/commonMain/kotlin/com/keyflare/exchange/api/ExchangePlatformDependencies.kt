package com.keyflare.exchange.api

import com.keyflare.common.datastore.DataStorePlatform
import com.keyflare.common.utils.AppBuildType
import com.keyflare.exchange.core.analytics.AnalyticsAgent
import com.keyflare.exchange.core.analytics.NoOpAnalyticsAgent
import com.keyflare.exchange.core.platform.PlatformServices

public data class ExchangePlatformDependencies(
    val dataStorePlatform: DataStorePlatform,
    val buildType: AppBuildType,
    val comicVineApiKey: String = "",
    val analyticsAgent: AnalyticsAgent = NoOpAnalyticsAgent,
    val platformServices: PlatformServices = PlatformServices(),
    val openNetworkLogs: (() -> Unit)? = null,
) {
    public constructor(
        dataStorePlatform: DataStorePlatform,
        buildType: AppBuildType,
        comicVineApiKey: String = "",
        openNetworkLogs: (() -> Unit)? = null,
    ) : this(
        dataStorePlatform = dataStorePlatform,
        buildType = buildType,
        comicVineApiKey = comicVineApiKey,
        analyticsAgent = NoOpAnalyticsAgent,
        platformServices = PlatformServices(),
        openNetworkLogs = openNetworkLogs,
    )
}
