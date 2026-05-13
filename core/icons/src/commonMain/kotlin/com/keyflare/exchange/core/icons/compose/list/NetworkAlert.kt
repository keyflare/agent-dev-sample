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

val AppIcon.Compose.NetworkAlert: ImageVector
    get() {
        if (networkAlert != null) {
            return networkAlert!!
        }

        networkAlert = ImageVector.Builder(
            name = AppIcon.NETWORK_ALERT.name,
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
                moveTo(360f, 120f)
                horizontalLineTo(600f)
                verticalLineTo(300f)
                horizontalLineTo(360f)
                close()

                moveTo(450f, 300f)
                horizontalLineTo(510f)
                verticalLineTo(390f)
                horizontalLineTo(450f)
                close()

                moveTo(390f, 390f)
                horizontalLineTo(570f)
                verticalLineTo(630f)
                horizontalLineTo(390f)
                close()

                moveTo(450f, 450f)
                horizontalLineTo(510f)
                verticalLineTo(555f)
                horizontalLineTo(450f)
                close()

                moveTo(450f, 570f)
                horizontalLineTo(510f)
                verticalLineTo(630f)
                horizontalLineTo(450f)
                close()

                moveTo(270f, 630f)
                horizontalLineTo(450f)
                verticalLineTo(690f)
                horizontalLineTo(270f)
                close()

                moveTo(510f, 630f)
                horizontalLineTo(690f)
                verticalLineTo(690f)
                horizontalLineTo(510f)
                close()

                moveTo(150f, 690f)
                horizontalLineTo(390f)
                verticalLineTo(870f)
                horizontalLineTo(150f)
                close()

                moveTo(570f, 690f)
                horizontalLineTo(810f)
                verticalLineTo(870f)
                horizontalLineTo(570f)
                close()
            }
        }.build()

        return networkAlert!!
    }

private var networkAlert: ImageVector? = null
