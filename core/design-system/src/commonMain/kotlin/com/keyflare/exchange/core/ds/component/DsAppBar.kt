package com.keyflare.exchange.core.ds.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.icons.compose.list.Back

@Composable
fun DsAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    subtitleColor: Color = CustomTheme.colors.textSecondary,
    caption: String? = null,
    dividerVisible: Boolean = false,
    background: Color = CustomTheme.colors.background,
    onBackClick: () -> Unit,
) {
    val density = LocalDensity.current
    val statusBarHeight = with (density) {
        WindowInsets.systemBars.getTop(density).toDp()
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp + statusBarHeight)
                .background(color = background)
                .padding(top = statusBarHeight)
        ) {
            Spacer(Modifier.size(8.dp))
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = AppIcon.Compose.Back,
                    contentDescription = null,
                    tint = CustomTheme.colors.textPrimary,
                )
            }
            Spacer(Modifier.size(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = CustomTheme.colors.textPrimary,
                    style = CustomTheme.type.heading1,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = subtitleColor,
                        style = CustomTheme.type.body2,
                    )
                }
            }
            if (caption != null) {
                Text(
                    text = caption,
                    color = CustomTheme.colors.textPrimaryVariant,
                    style = CustomTheme.type.body2,
                    modifier = Modifier.padding(end = 12.dp, top = 4.dp),
                )
            }
        }
        if (dividerVisible) {
            Divider(
                color = CustomTheme.colors.divider,
                thickness = 0.7.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
