package com.keyflare.exchange.core.ds.common

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Immutable
data class CustomColors(
    val textPrimary: Color = Color.White,
    val textAction: Color = Color.White,
    val textPrimaryVariant: Color = Color.White,
    val textSecondary: Color = Color.White,
    val textAdditional: Color = Color.White,
    val textAlert: Color = Color.White,
    val textSuccess: Color = Color.White,
    val background: Color = Color.White,
    val surface: Color = Color.White,
    val surfaceSecondary: Color = Color.White,
    val surfaceSecondaryVariant: Color = Color.White,
    val surfaceAction: Color = Color.White,
    val surfaceAccent: Color = Color.White,
    val iconsPrimary: Color = Color.White,
    val iconsSecondary: Color = Color.White,
    val iconsAction: Color = Color.White,
    val border: Color = Color.White,
    val divider: Color = Color.White,
    val tint: Color = Color.White,
    val isDark: Boolean = false,
)

@Immutable
data class CustomTypography(
    val digitL: TextStyle = TextStyle.Default,
    val digitM: TextStyle = TextStyle.Default,
    val digitS: TextStyle = TextStyle.Default,
    val heading1: TextStyle = TextStyle.Default,
    val heading2: TextStyle = TextStyle.Default,
    val heading3: TextStyle = TextStyle.Default,
    val heading4: TextStyle = TextStyle.Default,
    val body1: TextStyle = TextStyle.Default,
    val body2: TextStyle = TextStyle.Default,
    val body3: TextStyle = TextStyle.Default,
    val body4: TextStyle = TextStyle.Default,
    val digitLMono: TextStyle = TextStyle.Default,
    val digitMMono: TextStyle = TextStyle.Default,
    val digitSMono: TextStyle = TextStyle.Default,
    val heading1Mono: TextStyle = TextStyle.Default,
    val heading2Mono: TextStyle = TextStyle.Default,
    val heading3Mono: TextStyle = TextStyle.Default,
    val heading4Mono: TextStyle = TextStyle.Default,
    val body1Mono: TextStyle = TextStyle.Default,
    val body2Mono: TextStyle = TextStyle.Default,
    val body3Mono: TextStyle = TextStyle.Default,
    val body4Mono: TextStyle = TextStyle.Default,
)

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }
val LocalCustomTypography = staticCompositionLocalOf { CustomTypography() }

object CustomTheme {
    val colors @Composable get() = LocalCustomColors.current
    val type @Composable get() = LocalCustomTypography.current
}

@Composable
fun CustomTheme(
    colors: CustomColors,
    type: CustomTypography,
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val textSelectionColors = remember {
        TextSelectionColors(
            handleColor = colors.textAction,
            backgroundColor = colors.textAction.copy(alpha = 0.2f),
        )
    }

    MaterialTheme(
        colors = if (darkTheme) darkColors() else lightColors(),
        content = {
            CompositionLocalProvider(
                LocalTextSelectionColors provides textSelectionColors,
                LocalCustomColors provides colors,
                LocalCustomTypography provides type,
                content = content,
            )
        },
    )
}
