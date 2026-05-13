package com.keyflare.exchange.core.ds.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.keyflare.exchange.core.ds.common.CustomTheme

@Composable
fun ExchangeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors

    CustomTheme(
        colors = colors,
        type = type(),
        darkTheme = darkTheme,
        content = content,
    )
}
