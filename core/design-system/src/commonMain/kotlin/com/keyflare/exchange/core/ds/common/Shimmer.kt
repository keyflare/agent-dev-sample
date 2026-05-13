package com.keyflare.exchange.core.ds.common

import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.LayoutDirection

/**
 * A modifier that applies content-masked shimmer.
 *
 * @param durationMillis The duration of one shimmer cycle in milliseconds.
 * @param delayMillis Delay before each shimmer cycle starts.
 * @param bandSize Relative size of the bright shimmer band.
 * @param colors The alpha mask colors used by the gradient.
 */
fun Modifier.shimmerMask(
    active: Boolean = true,
    durationMillis: Int = 1500,
    delayMillis: Int = 250,
    bandSize: Float = 0.3f,
    colors: List<Color> = listOf(
        Color.Black.copy(alpha = 0.3f),
        Color.Black,
        Color.Black.copy(alpha = 0.3f),
    )
): Modifier = if (!active) {
    this
} else composed {
    val layoutDirection = LocalLayoutDirection.current
    val transition = rememberInfiniteTransition()
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart
        ),
    )

    this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithCache {
            val shimmerFrame = calculateShimmerFrame(
                progress = progress,
                size = size,
                bandSize = bandSize,
                layoutDirection = layoutDirection,
            )
            val brush = Brush.linearGradient(
                colors = colors,
                start = shimmerFrame.start,
                end = shimmerFrame.end,
            )
            onDrawWithContent {
                drawContent()
                drawRect(
                    brush = brush,
                    blendMode = BlendMode.DstIn,
                )
            }
        }
}

internal data class ShimmerFrame(
    val start: Offset,
    val end: Offset,
)

internal fun calculateShimmerFrame(
    progress: Float,
    size: Size,
    bandSize: Float,
    layoutDirection: LayoutDirection,
): ShimmerFrame {
    val min = -bandSize
    val max = 1f + bandSize

    val initialStart = if (layoutDirection == LayoutDirection.Rtl) {
        Offset(x = max, y = min)
    } else {
        Offset(x = min, y = min)
    }
    val targetStart = if (layoutDirection == LayoutDirection.Rtl) {
        Offset(x = 0f, y = 1f)
    } else {
        Offset(x = 1f, y = 1f)
    }
    val initialEnd = if (layoutDirection == LayoutDirection.Rtl) {
        Offset(x = 1f, y = 0f)
    } else {
        Offset.Zero
    }
    val targetEnd = if (layoutDirection == LayoutDirection.Rtl) {
        Offset(x = min, y = max)
    } else {
        Offset(x = max, y = max)
    }

    return ShimmerFrame(
        start = lerpOffset(initialStart, targetStart, progress).toAbsolute(size),
        end = lerpOffset(initialEnd, targetEnd, progress).toAbsolute(size),
    )
}

private fun lerpOffset(start: Offset, end: Offset, fraction: Float): Offset = Offset(
    x = start.x + (end.x - start.x) * fraction,
    y = start.y + (end.y - start.y) * fraction,
)

private fun Offset.toAbsolute(size: Size): Offset = Offset(
    x = x * size.width,
    y = y * size.height,
)
