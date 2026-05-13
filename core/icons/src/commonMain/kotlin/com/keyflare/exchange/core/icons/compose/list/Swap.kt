package com.keyflare.exchange.core.icons.compose.list

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.icons.AppIcon

val AppIcon.Compose.Swap: ImageVector
    get() {
        if (swap != null) return swap!!

        swap = ImageVector.Builder(
            name = AppIcon.SWAP.name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFe3e3e3))
            ) {
                moveTo(320f, 520f)
                verticalLineToRelative(-287f)
                lineTo(217f, 336f)
                lineToRelative(-57f, -56f)
                lineToRelative(200f, -200f)
                lineToRelative(200f, 200f)
                lineToRelative(-57f, 56f)
                lineToRelative(-103f, -103f)
                verticalLineToRelative(287f)
                horizontalLineToRelative(-80f)
                close()
                moveTo(600f, 880f)
                lineTo(400f, 680f)
                lineToRelative(57f, -56f)
                lineToRelative(103f, 103f)
                verticalLineToRelative(-287f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(287f)
                lineToRelative(103f, -103f)
                lineToRelative(57f, 56f)
                lineTo(600f, 880f)
                close()
            }
        }.build()

        return swap!!
    }

private var swap: ImageVector? = null

