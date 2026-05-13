package com.keyflare.exchange.feature.debugpanel.api

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.icons.compose.imageVector
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.feature.debugpanel.api.screen.preview.ComponentsPreviewViewState

@Composable
internal actual fun CustomDebugPanelScreenRenderer(
    state: UtilityScreenViewState.Screen.Custom,
) {
    when (state) {
        is ComponentsPreviewViewState -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(
                    items = state.items,
                    key = { item -> item.name },
                ) { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(
                                    color = CustomTheme.colors.surface,
                                    shape = RoundedCornerShape(16.dp),
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = requireNotNull(item.icon.imageVector()),
                                contentDescription = item.name,
                                tint = CustomTheme.colors.iconsPrimary,
                            )
                        }

                        Text(
                            text = item.name,
                            style = CustomTheme.type.body3,
                            color = CustomTheme.colors.textSecondary,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 4.dp),
                        )
                    }
                }
            }
        }

        else -> Unit
    }
}
