package com.keyflare.exchange.impl.network

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.keyflare.exchange.api.ExchangePlatformDependencies
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun createHttpClient(
    platformDependencies: ExchangePlatformDependencies,
): HttpClient {
    val context = platformDependencies.dataStorePlatform.context

    return HttpClient(OkHttp) {
        installCommonHttpClientPlugins()
        engine {
            addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .alwaysReadResponseBody(true)
                    .build()
            )
        }
    }
}
