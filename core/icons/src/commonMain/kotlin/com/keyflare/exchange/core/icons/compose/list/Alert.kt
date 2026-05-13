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

val AppIcon.Compose.Alert: ImageVector
    get() {
        if (alert != null) {
            return alert!!
        }

        alert = ImageVector.Builder(
            name = AppIcon.ALERT.name,
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
                moveTo(480f, 160f)
                lineTo(820f, 760f)
                horizontalLineTo(140f)
                close()

                moveTo(440f, 360f)
                horizontalLineTo(520f)
                verticalLineTo(560f)
                horizontalLineTo(440f)
                close()

                moveTo(440f, 620f)
                horizontalLineTo(520f)
                verticalLineTo(700f)
                horizontalLineTo(440f)
                close()
            }
        }.build()

        return alert!!
    }

private var alert: ImageVector? = null
