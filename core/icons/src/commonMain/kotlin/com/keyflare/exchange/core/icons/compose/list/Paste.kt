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

val AppIcon.Compose.Paste: ImageVector
	get() {
		if (paste != null) {
			return paste!!
		}
		paste = ImageVector.Builder(
            name = AppIcon.PASTE.name,
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
				moveTo(200f, 840f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(120f, 760f)
				verticalLineToRelative(-560f)
				quadToRelative(0f, -33f, 23.5f, -56.5f)
				reflectiveQuadTo(200f, 120f)
				horizontalLineToRelative(167f)
				quadToRelative(11f, -35f, 43f, -57.5f)
				reflectiveQuadToRelative(70f, -22.5f)
				quadToRelative(40f, 0f, 71.5f, 22.5f)
				reflectiveQuadTo(594f, 120f)
				horizontalLineToRelative(166f)
				quadToRelative(33f, 0f, 56.5f, 23.5f)
				reflectiveQuadTo(840f, 200f)
				verticalLineToRelative(560f)
				quadToRelative(0f, 33f, -23.5f, 56.5f)
				reflectiveQuadTo(760f, 840f)
				horizontalLineTo(200f)
				close()
				moveToRelative(0f, -80f)
				horizontalLineToRelative(560f)
				verticalLineToRelative(-560f)
				horizontalLineToRelative(-80f)
				verticalLineToRelative(120f)
				horizontalLineTo(280f)
				verticalLineToRelative(-120f)
				horizontalLineToRelative(-80f)
				verticalLineToRelative(560f)
				close()
				moveToRelative(280f, -560f)
				quadToRelative(17f, 0f, 28.5f, -11.5f)
				reflectiveQuadTo(520f, 160f)
				quadToRelative(0f, -17f, -11.5f, -28.5f)
				reflectiveQuadTo(480f, 120f)
				quadToRelative(-17f, 0f, -28.5f, 11.5f)
				reflectiveQuadTo(440f, 160f)
				quadToRelative(0f, 17f, 11.5f, 28.5f)
				reflectiveQuadTo(480f, 200f)
				close()
			}
		}.build()
		return paste!!
	}

private var paste: ImageVector? = null
