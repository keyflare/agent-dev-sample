package com.keyflare.exchange.feature.debugpanel

import kotlinx.serialization.Serializable

@Serializable
sealed interface DebugPanelArgs {

    @Serializable
    data object StartScreenArgs : DebugPanelArgs

    @Serializable
    data object PreviewScreenArgs : DebugPanelArgs
}
