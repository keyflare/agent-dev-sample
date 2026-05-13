package com.keyflare.exchange.android

import com.keyflare.exchange.api.ExchangeApp
import com.keyflare.exchange.api.ExchangePlatformDependencies

object ExchangeAppHolder {
    lateinit var exchangeApp: ExchangeApp
        private set

    fun initialize(platformDependencies: ExchangePlatformDependencies) {
        exchangeApp = ExchangeApp(platformDependencies)
    }
}
