package com.keyflare.exchange.core.ds.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.ds.common.trueShadow

@Composable
fun DsSegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return

    val outerShape = RoundedCornerShape(8.dp)
    val selectedShape = RoundedCornerShape(6.dp)
    val safeSelectedIndex = selectedIndex.coerceIn(items.indices)

    BoxWithConstraints(
        modifier = modifier
            .clip(outerShape)
            .background(CustomTheme.colors.surfaceSecondary)
            .padding(3.dp)
    ) {
        val itemWidth = maxWidth / items.size
        val selectedOffset by animateDpAsState(
            targetValue = itemWidth * safeSelectedIndex,
            animationSpec = tween(durationMillis = 220),
        )

        Box(
            modifier = Modifier
                .offset(x = selectedOffset)
                .width(itemWidth)
                .fillMaxHeight()
                .trueShadow(
                    shape = selectedShape,
                    offset = androidx.compose.ui.unit.DpOffset(x = 0.dp, y = 1.dp),
                    radius = 4.dp,
                    color = Color.Black.copy(alpha = 0.12f),
                )
                .clip(selectedShape)
                .background(CustomTheme.colors.surface)
        )

        Row(modifier = Modifier.fillMaxSize()) {
            items.forEachIndexed { index, item ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(selectedShape)
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource(),
                        ) { onSelectedIndexChange(index) }
                ) {
                    Text(
                        text = item,
                        style = CustomTheme.type.body2,
                        color = CustomTheme.colors.textPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
