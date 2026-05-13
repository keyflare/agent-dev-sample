package com.keyflare.exchange.impl.network

import com.keyflare.exchange.api.ExchangePlatformDependencies
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

internal actual fun createHttpClient(
    platformDependencies: ExchangePlatformDependencies,
): HttpClient {
    return HttpClient(Darwin) {
        installCommonHttpClientPlugins()
        defaultRequest {
            header(HttpHeaders.AcceptEncoding, "identity")
        }
    }
}
