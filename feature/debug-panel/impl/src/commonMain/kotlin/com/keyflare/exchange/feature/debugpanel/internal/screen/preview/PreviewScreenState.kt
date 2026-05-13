package com.keyflare.exchange.feature.debugpanel.internal.screen.preview

import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.feature.debugpanel.api.screen.preview.ComponentsPreviewViewState

internal data object PreviewScreenState {

    fun toViewState(): ComponentsPreviewViewState {
        return ComponentsPreviewViewState(
            title = "Icons Preview",
            items = AppIcon.entries.map { icon ->
                ComponentsPreviewViewState.Item(
                    icon = icon,
                    name = icon.name,
                )
            },
        )
    }
}
