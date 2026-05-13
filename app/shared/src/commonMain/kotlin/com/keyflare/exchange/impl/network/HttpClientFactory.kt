package com.keyflare.exchange.impl.network

import com.keyflare.exchange.api.ExchangePlatformDependencies
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

internal expect fun createHttpClient(
    platformDependencies: ExchangePlatformDependencies,
): HttpClient

internal fun HttpClientConfig<*>.installCommonHttpClientPlugins() {
    install(ContentNegotiation) { json() }
}
