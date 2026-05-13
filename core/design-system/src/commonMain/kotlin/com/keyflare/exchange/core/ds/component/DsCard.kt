package com.keyflare.exchange.core.ds.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.ds.common.addIf
import com.keyflare.exchange.core.ds.common.trueShadow
import com.keyflare.exchange.core.ds.theme.ExchangeTheme

@Composable
fun DsCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    onClick: (() -> Unit)? = null,
    shadow: Boolean = true,
    border: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(size = cornerRadius)

    Box(
        modifier = modifier
            .addIf(shadow) {
                trueShadow(
                    shape = shape,
                    offset = DpOffset(x = 0.dp, y = 4.dp),
                    radius = 40.dp,
                    color = Color(0, 0, 0, 18)
                )
            }
            .clip(shape = shape)
            .background(color = CustomTheme.colors.surface)
            .addIf(border) {
                border(
                    width = 0.5.dp,
                    color = CustomTheme.colors.border,
                    shape = shape,
                )
            }
            .addIf(onClick != null) {
                clickable(
                    enabled = onClick != null,
                    onClick = { onClick?.invoke() }
                )
            },
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
private fun DsCardCommonPreview() {
    Column {
        ExchangeTheme {
            Box(modifier = Modifier.background(color = CustomTheme.colors.background)) {
                DsCard(
                    modifier = Modifier.padding(16.dp),
                    shadow = false,
                ) {
                    Text(
                        text = "CommonMain preview",
                        color = CustomTheme.colors.textPrimary,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
        ExchangeTheme(darkTheme = true) {
            Box(modifier = Modifier.background(color = CustomTheme.colors.background)) {
                DsCard(
                    modifier = Modifier.padding(16.dp),
                    shadow = false,
                ) {
                    Text(
                        text = "CommonMain preview",
                        color = CustomTheme.colors.textPrimary,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }

}
