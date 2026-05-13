package com.keyflare.exchange.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.keyflare.exchange.api.ExchangeRootComponent

class MainActivity : ComponentActivity() {
    private lateinit var component: ExchangeRootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        component = ExchangeAppHolder
            .exchangeApp
            .onPlatformCreate(defaultComponentContext())

        setContent {
            ExchangeAppView(component = component)
        }
    }
}
