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

val AppIcon.Compose.Back: ImageVector
	get() {
		if (back != null) {
			return back!!
		}
		back = ImageVector.Builder(
            name = AppIcon.BACK.name,
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
				moveTo(313f, 520f)
				lineToRelative(224f, 224f)
				lineToRelative(-57f, 56f)
				lineToRelative(-320f, -320f)
				lineToRelative(320f, -320f)
				lineToRelative(57f, 56f)
				lineToRelative(-224f, 224f)
				horizontalLineToRelative(487f)
				verticalLineToRelative(80f)
				horizontalLineTo(313f)
				close()
			}
		}.build()
		return back!!
	}

private var back: ImageVector? = null
