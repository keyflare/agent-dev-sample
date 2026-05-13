package com.keyflare.exchange.core.icons.compose.list

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.icons.AppIcon

val AppIcon.Compose.Exchange: ImageVector
    get() {
        if (exchange != null) {
            return exchange!!
        }
        exchange = Builder(
            name = AppIcon.EXCHANGE.name,
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 196.0f,
            viewportHeight = 196.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(149.03f, 145.54f)
                horizontalLineTo(156.62f)
                curveTo(156.79f, 145.54f, 156.95f, 145.57f, 157.1f, 145.63f)
                curveTo(157.26f, 145.7f, 157.4f, 145.79f, 157.51f, 145.91f)
                curveTo(157.63f, 146.02f, 157.72f, 146.16f, 157.79f, 146.32f)
                curveTo(157.85f, 146.47f, 157.88f, 146.63f, 157.88f, 146.8f)
                verticalLineTo(153.66f)
                curveTo(157.88f, 153.83f, 157.85f, 153.99f, 157.79f, 154.15f)
                curveTo(157.72f, 154.3f, 157.63f, 154.44f, 157.51f, 154.55f)
                curveTo(157.4f, 154.67f, 157.26f, 154.76f, 157.1f, 154.83f)
                curveTo(156.95f, 154.89f, 156.79f, 154.92f, 156.62f, 154.92f)
                horizontalLineTo(144.1f)
                curveTo(143.77f, 154.92f, 143.45f, 155.06f, 143.21f, 155.29f)
                curveTo(142.98f, 155.53f, 142.84f, 155.85f, 142.84f, 156.18f)
                verticalLineTo(161.69f)
                curveTo(142.84f, 162.03f, 142.98f, 162.35f, 143.21f, 162.58f)
                curveTo(143.45f, 162.82f, 143.77f, 162.95f, 144.1f, 162.95f)
                horizontalLineTo(156.62f)
                curveTo(156.79f, 162.95f, 156.95f, 162.98f, 157.1f, 163.05f)
                curveTo(157.26f, 163.11f, 157.4f, 163.2f, 157.51f, 163.32f)
                curveTo(157.63f, 163.44f, 157.72f, 163.58f, 157.79f, 163.73f)
                curveTo(157.85f, 163.88f, 157.88f, 164.04f, 157.88f, 164.21f)
                verticalLineTo(170.96f)
                curveTo(157.88f, 171.13f, 157.85f, 171.29f, 157.79f, 171.45f)
                curveTo(157.72f, 171.6f, 157.63f, 171.74f, 157.51f, 171.85f)
                curveTo(157.4f, 171.97f, 157.26f, 172.06f, 157.1f, 172.13f)
                curveTo(156.95f, 172.19f, 156.79f, 172.22f, 156.62f, 172.22f)
                horizontalLineTo(144.1f)
                curveTo(143.77f, 172.22f, 143.45f, 172.35f, 143.21f, 172.59f)
                curveTo(142.98f, 172.83f, 142.84f, 173.15f, 142.84f, 173.48f)
                verticalLineTo(184.08f)
                curveTo(142.84f, 184.24f, 142.81f, 184.41f, 142.75f, 184.56f)
                curveTo(142.68f, 184.71f, 142.59f, 184.85f, 142.48f, 184.97f)
                curveTo(142.36f, 185.09f, 142.22f, 185.18f, 142.07f, 185.24f)
                curveTo(141.91f, 185.3f, 141.75f, 185.34f, 141.59f, 185.34f)
                horizontalLineTo(126.13f)
                curveTo(125.96f, 185.34f, 125.8f, 185.3f, 125.64f, 185.24f)
                curveTo(125.49f, 185.18f, 125.35f, 185.09f, 125.24f, 184.97f)
                curveTo(125.12f, 184.85f, 125.03f, 184.71f, 124.96f, 184.56f)
                curveTo(124.9f, 184.41f, 124.87f, 184.24f, 124.87f, 184.08f)
                verticalLineTo(173.48f)
                curveTo(124.87f, 173.15f, 124.73f, 172.83f, 124.5f, 172.59f)
                curveTo(124.26f, 172.35f, 123.94f, 172.22f, 123.61f, 172.22f)
                horizontalLineTo(111.2f)
                curveTo(110.87f, 172.22f, 110.55f, 172.09f, 110.31f, 171.85f)
                curveTo(110.07f, 171.62f, 109.94f, 171.3f, 109.94f, 170.96f)
                verticalLineTo(164.21f)
                curveTo(109.94f, 163.88f, 110.07f, 163.56f, 110.31f, 163.32f)
                curveTo(110.55f, 163.08f, 110.87f, 162.95f, 111.2f, 162.95f)
                horizontalLineTo(123.61f)
                curveTo(123.94f, 162.95f, 124.26f, 162.82f, 124.5f, 162.58f)
                curveTo(124.73f, 162.35f, 124.87f, 162.03f, 124.87f, 161.69f)
                verticalLineTo(156.18f)
                curveTo(124.87f, 155.85f, 124.73f, 155.53f, 124.5f, 155.29f)
                curveTo(124.26f, 155.06f, 123.94f, 154.92f, 123.61f, 154.92f)
                horizontalLineTo(111.2f)
                curveTo(110.87f, 154.92f, 110.55f, 154.79f, 110.31f, 154.55f)
                curveTo(110.07f, 154.32f, 109.94f, 154.0f, 109.94f, 153.66f)
                verticalLineTo(146.8f)
                curveTo(109.94f, 146.63f, 109.97f, 146.47f, 110.04f, 146.32f)
                curveTo(110.1f, 146.16f, 110.19f, 146.02f, 110.31f, 145.91f)
                curveTo(110.43f, 145.79f, 110.57f, 145.7f, 110.72f, 145.63f)
                curveTo(110.87f, 145.57f, 111.04f, 145.54f, 111.2f, 145.54f)
                horizontalLineTo(118.56f)
                curveTo(119.52f, 145.54f, 120.12f, 144.52f, 119.67f, 143.68f)
                lineTo(101.69f, 110.77f)
                curveTo(101.23f, 109.93f, 101.84f, 108.91f, 102.79f, 108.91f)
                horizontalLineTo(119.08f)
                curveTo(119.32f, 108.91f, 119.56f, 108.98f, 119.76f, 109.11f)
                curveTo(119.96f, 109.24f, 120.13f, 109.43f, 120.22f, 109.65f)
                lineTo(132.98f, 138.1f)
                curveTo(133.43f, 139.09f, 134.84f, 139.09f, 135.28f, 138.09f)
                lineTo(147.82f, 109.66f)
                curveTo(147.92f, 109.43f, 148.09f, 109.25f, 148.29f, 109.11f)
                curveTo(148.49f, 108.98f, 148.73f, 108.91f, 148.98f, 108.91f)
                horizontalLineTo(164.7f)
                curveTo(165.65f, 108.91f, 166.26f, 109.93f, 165.8f, 110.77f)
                lineTo(147.92f, 143.68f)
                curveTo(147.47f, 144.52f, 148.07f, 145.54f, 149.03f, 145.54f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(71.55f, 25.14f)
                curveTo(77.23f, 26.17f, 81.89f, 28.26f, 85.89f, 31.27f)
                curveTo(87.02f, 32.13f, 87.16f, 33.66f, 86.2f, 34.68f)
                lineTo(81.32f, 39.83f)
                curveTo(80.41f, 40.79f, 78.84f, 40.95f, 77.7f, 40.21f)
                curveTo(73.31f, 37.35f, 68.75f, 35.99f, 63.52f, 35.99f)
                curveTo(57.52f, 35.99f, 53.73f, 38.29f, 53.73f, 42.38f)
                curveTo(53.73f, 46.58f, 56.51f, 48.28f, 67.97f, 51.47f)
                curveTo(81.99f, 55.47f, 90.0f, 60.76f, 90.0f, 72.94f)
                curveTo(90.0f, 82.61f, 83.19f, 90.02f, 71.39f, 92.81f)
                curveTo(70.22f, 93.08f, 69.42f, 94.04f, 69.42f, 95.12f)
                verticalLineTo(103.59f)
                curveTo(69.42f, 104.23f, 69.14f, 104.84f, 68.63f, 105.29f)
                curveTo(68.13f, 105.75f, 67.45f, 106.0f, 66.74f, 106.0f)
                horizontalLineTo(59.31f)
                curveTo(58.6f, 106.0f, 57.91f, 105.75f, 57.41f, 105.29f)
                curveTo(56.91f, 104.84f, 56.63f, 104.23f, 56.63f, 103.59f)
                verticalLineTo(95.98f)
                curveTo(56.63f, 94.78f, 55.65f, 93.75f, 54.33f, 93.6f)
                curveTo(45.94f, 92.61f, 39.42f, 89.76f, 34.54f, 86.1f)
                curveTo(33.37f, 85.23f, 32.41f, 84.5f, 33.43f, 83.5f)
                lineTo(40.1f, 76.92f)
                curveTo(41.57f, 75.47f, 42.28f, 76.19f, 43.38f, 76.95f)
                curveTo(48.16f, 80.27f, 53.59f, 82.13f, 59.96f, 82.13f)
                curveTo(67.19f, 82.13f, 71.53f, 79.14f, 71.53f, 74.14f)
                curveTo(71.53f, 69.35f, 68.86f, 67.15f, 58.18f, 64.16f)
                curveTo(41.5f, 59.56f, 35.6f, 53.37f, 35.6f, 43.58f)
                curveTo(35.6f, 33.97f, 43.22f, 27.21f, 54.54f, 25.05f)
                curveTo(55.76f, 24.81f, 56.63f, 25.09f, 56.63f, 23.96f)
                verticalLineTo(14.08f)
                curveTo(56.63f, 12.75f, 57.83f, 13.02f, 59.31f, 13.02f)
                horizontalLineTo(66.74f)
                curveTo(67.09f, 13.02f, 67.44f, 13.09f, 67.76f, 13.21f)
                curveTo(68.09f, 13.33f, 68.39f, 13.51f, 68.63f, 13.73f)
                curveTo(68.88f, 13.95f, 69.08f, 14.22f, 69.21f, 14.51f)
                curveTo(69.35f, 14.8f, 69.42f, 15.11f, 69.42f, 15.43f)
                verticalLineTo(22.79f)
                curveTo(69.42f, 23.93f, 70.31f, 24.91f, 71.55f, 25.14f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(158.44f, 51.53f)
                lineTo(146.33f, 63.64f)
                lineTo(114.24f, 31.55f)
                curveTo(112.21f, 29.52f, 108.93f, 29.52f, 106.91f, 31.55f)
                lineTo(101.23f, 37.23f)
                curveTo(99.2f, 39.25f, 99.2f, 42.53f, 101.23f, 44.56f)
                lineTo(133.32f, 76.65f)
                lineTo(121.21f, 88.76f)
                curveTo(118.43f, 91.53f, 120.4f, 96.28f, 124.32f, 96.28f)
                horizontalLineTo(161.55f)
                curveTo(163.98f, 96.28f, 165.96f, 94.3f, 165.96f, 91.87f)
                verticalLineTo(54.64f)
                curveTo(165.96f, 50.72f, 161.21f, 48.76f, 158.44f, 51.53f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(94.77f, 170.1f)
                lineTo(62.68f, 138.01f)
                lineTo(74.79f, 125.9f)
                curveTo(77.57f, 123.12f, 75.6f, 118.38f, 71.68f, 118.38f)
                horizontalLineTo(34.45f)
                curveTo(32.02f, 118.38f, 30.04f, 120.35f, 30.04f, 122.79f)
                verticalLineTo(160.01f)
                curveTo(30.04f, 163.94f, 34.79f, 165.9f, 37.56f, 163.13f)
                lineTo(49.67f, 151.02f)
                lineTo(81.76f, 183.11f)
                curveTo(83.79f, 185.13f, 87.07f, 185.13f, 89.09f, 183.11f)
                lineTo(94.77f, 177.43f)
                curveTo(96.8f, 175.4f, 96.8f, 172.12f, 94.77f, 170.1f)
                close()
            }
        }
            .build()
        return exchange!!
    }

private var exchange: ImageVector? = null
