package com.keyflare.exchange.api

import com.keyflare.common.datastore.DataStorePlatform
import com.keyflare.common.utils.AppBuildType
import com.keyflare.exchange.core.analytics.AnalyticsAgent
import com.keyflare.exchange.core.analytics.NoOpAnalyticsAgent
import com.keyflare.exchange.core.platform.PlatformServices

public data class ExchangePlatformDependencies(
    val dataStorePlatform: DataStorePlatform,
    val buildType: AppBuildType,
    val analyticsAgent: AnalyticsAgent = NoOpAnalyticsAgent,
    val platformServices: PlatformServices = PlatformServices(),
    val openNetworkLogs: (() -> Unit)? = null,
    val comicVineApiKey: String = "",
) {
    public constructor(
        dataStorePlatform: DataStorePlatform,
        buildType: AppBuildType,
        openNetworkLogs: (() -> Unit)?,
    ) : this(
        dataStorePlatform = dataStorePlatform,
        buildType = buildType,
        analyticsAgent = NoOpAnalyticsAgent,
        platformServices = PlatformServices(),
        openNetworkLogs = openNetworkLogs,
    )

    public constructor(
        dataStorePlatform: DataStorePlatform,
        buildType: AppBuildType,
        comicVineApiKey: String,
        openNetworkLogs: (() -> Unit)? = null,
    ) : this(
        dataStorePlatform = dataStorePlatform,
        buildType = buildType,
        analyticsAgent = NoOpAnalyticsAgent,
        platformServices = PlatformServices(),
        openNetworkLogs = openNetworkLogs,
        comicVineApiKey = comicVineApiKey,
    )
}
