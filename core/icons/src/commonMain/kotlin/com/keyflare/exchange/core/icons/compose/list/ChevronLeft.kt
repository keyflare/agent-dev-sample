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

val AppIcon.Compose.ChevronLeft: ImageVector
	get() {
		if (chevronLeft != null) {
			return chevronLeft!!
		}
		chevronLeft = ImageVector.Builder(
            name = AppIcon.CHEVRON_LEFT.name,
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
				moveTo(560f, 720f)
				lineTo(320f, 480f)
				lineToRelative(240f, -240f)
				lineToRelative(56f, 56f)
				lineToRelative(-184f, 184f)
				lineToRelative(184f, 184f)
				lineToRelative(-56f, 56f)
				close()
			}
		}.build()
		return chevronLeft!!
	}

private var chevronLeft: ImageVector? = null
