package com.keyflare.exchange.core.icons.compose.list

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.icons.AppIcon

val AppIcon.Compose.ThumbUp: ImageVector
    get() {
        if (thumbUp != null) {
            return thumbUp!!
        }
        thumbUp = ImageVector.Builder(
            name = AppIcon.THUMB_UP.name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            addPath(
                pathData = PathParser().parsePathString("M18 21H7V8l7-7 1.25 1.25q0.175 0.175 0.2875 0.475t0.1125 0.575v0.35L14.55 8H21q0.8 0 1.4 0.6t0.6 1.4v2q0 0.175-0.05 0.375t-0.1 0.375l-3 7.05q-0.225 0.5-0.75 0.85T18 21Zm-9-2h9l3-7v-2h-9l1.35-5.5L9 8.85V19Zm-2-11v2H4v9h3v2H2V8h5Z").toNodes(),
                pathFillType = PathFillType.NonZero,
                fill = SolidColor(Color(0xFFE8EAED)),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 4f,
            )
        }.build()
        return thumbUp!!
    }

private var thumbUp: ImageVector? = null
