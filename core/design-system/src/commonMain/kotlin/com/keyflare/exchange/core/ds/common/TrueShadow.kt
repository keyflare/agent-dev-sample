@file:Suppress("unused")

package com.keyflare.exchange.core.ds.common

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

fun Modifier.trueShadow(
    shape: Shape = RectangleShape,
    offset: DpOffset = DpOffset(0.dp, 4.dp),
    radius: Dp = 10.dp,
    spread: Dp = 0.dp,
    color: Color = Color(0f, 0f, 0f, 0.25f),
    type: TrueShadow.Type = TrueShadow.Type.OUTER,
): Modifier = this.then(TrueShadow(offset, radius, spread, color, type).modifier(shape))

fun Modifier.trueShadow(
    shape: Shape = RectangleShape,
    shadow: TrueShadow,
) = this.then(shadow.modifier(shape))

data class TrueShadow(
    val offset: DpOffset,
    val radius: Dp,
    val spread: Dp,
    val color: Color,
    val type: Type = Type.OUTER,
) {
    enum class Type { INNER, OUTER }
}

expect fun TrueShadow.modifier(shape: Shape): DrawModifier
