package com.keyflare.exchange.core.ds.common

import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope

actual fun TrueShadow.modifier(shape: Shape): DrawModifier {
    return EmptyDrawModifier()
}

private class EmptyDrawModifier : DrawModifier {
    override fun ContentDrawScope.draw() {
        drawContent()
    }
}
