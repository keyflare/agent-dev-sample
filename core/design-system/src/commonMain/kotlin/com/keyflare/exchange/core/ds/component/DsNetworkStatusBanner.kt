package com.keyflare.exchange.core.ds.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.icons.compose.imageVector
import com.keyflare.exchange.core.icons.compose.list.Alert
import com.keyflare.exchange.core.icons.compose.list.NetworkAlert

public data class DsNetworkStatusBannerState(
    val kind: Kind,
    val title: String,
    val message: String,
) {
    public enum class Kind {
        NetworkIssue,
        Alert,
    }
}

@Composable
public fun AnimatedDsNetworkStatusBanner(
    banner: DsNetworkStatusBannerState?,
    modifier: Modifier = Modifier,
) {
    var displayedBanner by remember { mutableStateOf(banner) }

    LaunchedEffect(banner) {
        if (banner != null) {
            displayedBanner = banner
        }
    }

    AnimatedVisibility(
        visible = banner != null,
        enter = fadeIn(animationSpec = tween(durationMillis = 220)) +
            expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(durationMillis = 220),
            ),
        exit = fadeOut(animationSpec = tween(durationMillis = 180)) +
            shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(durationMillis = 180),
            ),
        modifier = modifier,
    ) {
        displayedBanner?.let { visibleBanner ->
            DsNetworkStatusBanner(
                banner = visibleBanner,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
public fun DsNetworkStatusBanner(
    banner: DsNetworkStatusBannerState,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .clip(shape)
            .background(CustomTheme.colors.surfaceAccent.copy(alpha = 0.15f))
            .border(
                width = 0.5.dp,
                color = CustomTheme.colors.surfaceAccent,
                shape = shape,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = dsNetworkStatusBannerIcon(banner).imageVector(
                    default = when (banner.kind) {
                        DsNetworkStatusBannerState.Kind.NetworkIssue -> AppIcon.Compose.NetworkAlert
                        DsNetworkStatusBannerState.Kind.Alert -> AppIcon.Compose.Alert
                    },
                ),
                contentDescription = null,
                tint = CustomTheme.colors.surfaceAccent,
                modifier = Modifier.size(20.dp),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = banner.title,
                    style = CustomTheme.type.heading4,
                    color = CustomTheme.colors.surfaceAccent,
                )
                Text(
                    text = banner.message,
                    style = CustomTheme.type.body3,
                    color = CustomTheme.colors.surfaceAccent,
                )
            }
        }
    }
}

internal fun dsNetworkStatusBannerIcon(
    banner: DsNetworkStatusBannerState,
): AppIcon = when (banner.kind) {
    DsNetworkStatusBannerState.Kind.NetworkIssue -> AppIcon.NETWORK_ALERT
    DsNetworkStatusBannerState.Kind.Alert -> AppIcon.ALERT
}
