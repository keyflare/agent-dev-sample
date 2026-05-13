package com.keyflare.exchange.core.ds.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.collapsingtoolbar.CollapsingToolbarScaffold
import com.keyflare.exchange.core.ds.common.collapsingtoolbar.CollapsingToolbarScaffoldScope
import com.keyflare.exchange.core.ds.common.collapsingtoolbar.CollapsingToolbarScope
import com.keyflare.exchange.core.ds.common.collapsingtoolbar.CollapsingToolbarState
import com.keyflare.exchange.core.ds.common.collapsingtoolbar.ScrollStrategy
import com.keyflare.exchange.core.ds.common.collapsingtoolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun DsCollapsingToolbarScaffold(
    collapsedHeight: Dp = 0.dp,
    toolbar: @Composable CollapsingToolbarScope.(CollapsingToolbarState) -> Unit,
    body: @Composable CollapsingToolbarScaffoldScope.() -> Unit,
) {
    val collapsingToolbarScaffoldState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = collapsingToolbarScaffoldState,
        scrollStrategy = ScrollStrategy.EnterAlways,
        body = body,
        toolbar = {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(collapsedHeight)
            )
            toolbar(collapsingToolbarScaffoldState.toolbarState)
        },
        modifier = Modifier
            .fillMaxSize()
            .clip(RectangleShape),
    )
}
