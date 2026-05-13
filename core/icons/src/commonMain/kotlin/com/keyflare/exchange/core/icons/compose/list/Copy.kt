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

val AppIcon.Compose.Copy: ImageVector
	get() {
		if (copy != null) {
			return copy!!
		}
		copy = ImageVector.Builder(
            name = AppIcon.COPY.name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
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
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(360f, 720f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(280f, 640f)
				verticalLineToRelative(-480f)
				quadToRelative(0f, -33f, 23.5f, -56.5f)
				reflectiveQuadTo(360f, 80f)
				horizontalLineToRelative(360f)
				quadToRelative(33f, 0f, 56.5f, 23.5f)
				reflectiveQuadTo(800f, 160f)
				verticalLineToRelative(480f)
				quadToRelative(0f, 33f, -23.5f, 56.5f)
				reflectiveQuadTo(720f, 720f)
				horizontalLineTo(360f)
				close()
				moveToRelative(0f, -80f)
				horizontalLineToRelative(360f)
				verticalLineToRelative(-480f)
				horizontalLineTo(360f)
				verticalLineToRelative(480f)
				close()
				moveTo(200f, 880f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(120f, 800f)
				verticalLineToRelative(-560f)
				horizontalLineToRelative(80f)
				verticalLineToRelative(560f)
				horizontalLineToRelative(440f)
				verticalLineToRelative(80f)
				horizontalLineTo(200f)
				close()
				moveToRelative(160f, -240f)
				verticalLineToRelative(-480f)
				verticalLineToRelative(480f)
				close()
			}
		}.build()
		return copy!!
	}

private var copy: ImageVector? = null
