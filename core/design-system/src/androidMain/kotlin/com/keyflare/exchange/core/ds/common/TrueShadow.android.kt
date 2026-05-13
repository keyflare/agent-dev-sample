package com.keyflare.exchange.core.ds.common

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas

actual fun TrueShadow.modifier(shape: Shape): DrawModifier {
    return if (type == TrueShadow.Type.OUTER) {
        TrueOuterShadowModifier(shadow = this, shape = shape)
    } else {
        TrueInnerShadowModifier(shadow = this, shape = shape)
    }
}

private class TrueOuterShadowModifier(
    private val shadow: TrueShadow,
    private val shape: Shape,
) : DrawModifier {

    override fun ContentDrawScope.draw() {
        val spreadPx = shadow.spread.toPx()
        val paint = getPaint(shadow.color, shadow.radius.toPx())
        val outline = shape.createOutline(
            size = size.applySpread(spreadPx),
            layoutDirection = layoutDirection,
            density = this
        )

        drawIntoCanvas { canvas ->
            canvas.translate(
                left = shadow.offset.x.toPx() - spreadPx,
                top = shadow.offset.y.toPx() - spreadPx,
            ) {
                drawOutline(outline, paint)
            }
        }

        drawContent()
    }

    private fun getPaint(color: Color, radiusPx: Float): Paint {
        return Paint().apply {
            this.color = color
            setBlurRadius(radiusPx)
        }
    }

    private fun Size.applySpread(spread: Float): Size {
        return copy(
            width = width + spread * 2,
            height = height + spread * 2,
        )
    }
}

private class TrueInnerShadowModifier(
    private val shadow: TrueShadow,
    private val shape: Shape,
) : DrawModifier {

    override fun ContentDrawScope.draw() {
        val spreadPx = shadow.spread.toPx()
        val radiusPx = shadow.radius.toPx()
        val outline = shape.createOutline(
            size = size,
            layoutDirection = layoutDirection,
            density = this
        )
        val innerOutline = shape.createOutline(
            size = size.applySpread(spreadPx),
            layoutDirection = layoutDirection,
            density = this
        )
        val paint = getPaint(color = shadow.color)
        val rect = Rect(Offset.Zero, size)

        drawContent()

        drawIntoCanvas { canvas ->
            canvas.saveLayer(rect, paint)
            canvas.drawOutline(outline, paint)

            canvas.translate(
                left = shadow.offset.x.toPx() + spreadPx,
                top = shadow.offset.y.toPx() + spreadPx,
            ) {
                drawOutline(innerOutline, paint.setupAsDstOut(radiusPx))
            }
        }
    }

    private fun getPaint(color: Color): Paint {
        return Paint().apply { this.color = color }
    }

    private fun Paint.setupAsDstOut(radiusPx: Float): Paint {
        asFrameworkPaint().apply {
            color = android.graphics.Color.BLACK
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            if (radiusPx != 0f) {
                maskFilter = BlurMaskFilter(radiusPx, BlurMaskFilter.Blur.NORMAL)
            }
        }
        return this
    }

    private fun Size.applySpread(spread: Float): Size {
        return copy(
            width = width - spread * 2,
            height = height - spread * 2,
        )
    }
}

private fun Canvas.drawOutline(outline: Outline, paint: Paint) {
    when (outline) {
        is Outline.Rectangle -> drawRect(outline.rect, paint)
        is Outline.Generic -> drawPath(outline.path, paint)
        is Outline.Rounded -> drawPath(
            path = Path().apply { addRoundRect(outline.roundRect) },
            paint = paint,
        )
    }
}

private inline fun Canvas.translate(
    left: Float,
    top: Float,
    block: Canvas.() -> Unit
) {
    translate(left, top)
    block()
    translate(-left, -top)
}

private fun Paint.setBlurRadius(radiusPx: Float): Paint {
    if (radiusPx != 0f) {
        asFrameworkPaint().apply {
            maskFilter = BlurMaskFilter(radiusPx, BlurMaskFilter.Blur.NORMAL)
        }
    }
    return this
}
