package com.keyflare.exchange.feature.debugpanel.api.screen.preview

import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState

public data class ComponentsPreviewViewState(
    override val title: String,
    val items: List<Item>,
) : UtilityScreenViewState.Screen.Custom {

    public data class Item(
        val icon: AppIcon,
        val name: String,
    )
}
