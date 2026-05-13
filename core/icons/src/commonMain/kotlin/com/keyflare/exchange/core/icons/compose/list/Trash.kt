package com.keyflare.exchange.core.icons.compose.list

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.icons.AppIcon

val AppIcon.Compose.Trash: ImageVector
    get() {
        if (trash != null) {
            return trash!!
        }
        trash = ImageVector.Builder(
            name = AppIcon.TRASH.name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(
                fill = SolidColor(Color(0xFFE8EAED)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(280f, 840f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                quadToRelative(-23.5f, -23.5f, -23.5f, -56.5f)
                lineTo(200f, 280f)
                lineTo(160f, 280f)
                lineTo(160f, 200f)
                lineTo(360f, 200f)
                lineTo(400f, 160f)
                lineTo(560f, 160f)
                lineTo(600f, 200f)
                lineTo(800f, 200f)
                lineTo(800f, 280f)
                lineTo(760f, 280f)
                lineTo(760f, 760f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                quadToRelative(-23.5f, 23.5f, -56.5f, 23.5f)
                lineTo(280f, 840f)
                close()
                moveTo(680f, 280f)
                lineTo(280f, 280f)
                lineTo(280f, 760f)
                lineTo(680f, 760f)
                lineTo(680f, 280f)
                close()
                moveTo(360f, 680f)
                lineTo(440f, 680f)
                lineTo(440f, 360f)
                lineTo(360f, 360f)
                lineTo(360f, 680f)
                close()
                moveTo(520f, 680f)
                lineTo(600f, 680f)
                lineTo(600f, 360f)
                lineTo(520f, 360f)
                lineTo(520f, 680f)
                close()
            }
        }.build()
        return trash!!
    }

private var trash: ImageVector? = null
