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

val AppIcon.Compose.CardSettings: ImageVector
	get() {
		if (cardSettings != null) {
			return cardSettings!!
		}
		cardSettings = ImageVector.Builder(
            name = AppIcon.CARD_SETTINGS.name,
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
				moveTo(160f, 720f)
				verticalLineToRelative(-320f)
				verticalLineToRelative(13f)
				verticalLineToRelative(-173f)
				verticalLineToRelative(480f)
				close()
				moveToRelative(0f, -400f)
				horizontalLineToRelative(640f)
				verticalLineToRelative(-80f)
				horizontalLineTo(160f)
				verticalLineToRelative(80f)
				close()
				moveToRelative(303f, 480f)
				horizontalLineTo(160f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(80f, 720f)
				verticalLineToRelative(-480f)
				quadToRelative(0f, -33f, 23.5f, -56.5f)
				reflectiveQuadTo(160f, 160f)
				horizontalLineToRelative(640f)
				quadToRelative(33f, 0f, 56.5f, 23.5f)
				reflectiveQuadTo(880f, 240f)
				verticalLineToRelative(213f)
				quadToRelative(-35f, -25f, -76.5f, -39f)
				reflectiveQuadTo(716f, 400f)
				quadToRelative(-57f, 0f, -107.5f, 21.5f)
				reflectiveQuadTo(520f, 480f)
				horizontalLineTo(160f)
				verticalLineToRelative(240f)
				horizontalLineToRelative(279f)
				quadToRelative(3f, 21f, 9f, 41f)
				reflectiveQuadToRelative(15f, 39f)
				close()
				moveToRelative(213f, 80f)
				lineToRelative(-12f, -60f)
				quadToRelative(-12f, -5f, -22.5f, -10.5f)
				reflectiveQuadTo(620f, 796f)
				lineToRelative(-58f, 18f)
				lineToRelative(-40f, -68f)
				lineToRelative(46f, -40f)
				quadToRelative(-2f, -13f, -2f, -26f)
				reflectiveQuadToRelative(2f, -26f)
				lineToRelative(-46f, -40f)
				lineToRelative(40f, -68f)
				lineToRelative(58f, 18f)
				quadToRelative(11f, -8f, 21.5f, -13.5f)
				reflectiveQuadTo(664f, 540f)
				lineToRelative(12f, -60f)
				horizontalLineToRelative(80f)
				lineToRelative(12f, 60f)
				quadToRelative(12f, 5f, 22.5f, 10.5f)
				reflectiveQuadTo(812f, 564f)
				lineToRelative(58f, -18f)
				lineToRelative(40f, 68f)
				lineToRelative(-46f, 40f)
				quadToRelative(2f, 13f, 2f, 26f)
				reflectiveQuadToRelative(-2f, 26f)
				lineToRelative(46f, 40f)
				lineToRelative(-40f, 68f)
				lineToRelative(-58f, -18f)
				quadToRelative(-11f, 8f, -21.5f, 13.5f)
				reflectiveQuadTo(768f, 820f)
				lineToRelative(-12f, 60f)
				horizontalLineToRelative(-80f)
				close()
				moveToRelative(40f, -120f)
				quadToRelative(33f, 0f, 56.5f, -23.5f)
				reflectiveQuadTo(796f, 680f)
				quadToRelative(0f, -33f, -23.5f, -56.5f)
				reflectiveQuadTo(716f, 600f)
				quadToRelative(-33f, 0f, -56.5f, 23.5f)
				reflectiveQuadTo(636f, 680f)
				quadToRelative(0f, 33f, 23.5f, 56.5f)
				reflectiveQuadTo(716f, 760f)
				close()
			}
		}.build()
		return cardSettings!!
	}

private var cardSettings: ImageVector? = null
