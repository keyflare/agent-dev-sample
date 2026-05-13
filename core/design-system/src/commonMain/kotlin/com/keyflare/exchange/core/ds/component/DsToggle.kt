package com.keyflare.exchange.core.ds.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.ds.common.trueShadow

@Composable
fun DsToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val trackWidth = 51.dp
    val trackHeight = 31.dp
    val trackInset = 2.dp
    val thumbSize = 27.dp
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - trackInset else trackInset,
        animationSpec = tween(durationMillis = 220),
    )
    val trackColor by animateColorAsState(
        targetValue = if (checked) {
            CustomTheme.colors.surfaceAction
        } else {
            CustomTheme.colors.surfaceSecondaryVariant
        },
        animationSpec = tween(durationMillis = 220),
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 32.dp)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = MutableInteractionSource(),
            ) { onCheckedChange(!checked) },
    ) {
        Text(
            text = label,
            style = CustomTheme.type.body2,
            color = CustomTheme.colors.textPrimaryVariant,
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
        )
        Box(
            modifier = Modifier
                .size(width = trackWidth, height = trackHeight)
                .clip(RoundedCornerShape(percent = 50))
                .background(trackColor.copy(alpha = if (enabled) 1f else 0.5f))
                .padding(trackInset),
            contentAlignment = Alignment.CenterStart,
        ) {
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset - trackInset)
                    .size(thumbSize)
                    .trueShadow(
                        shape = CircleShape,
                        radius = 4.dp,
                        offset = androidx.compose.ui.unit.DpOffset(x = 0.dp, y = 1.dp),
                        color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.18f),
                    )
                    .clip(CircleShape)
                    .background(CustomTheme.colors.surface)
            )
        }
    }
}
