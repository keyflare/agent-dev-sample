package com.keyflare.exchange.feature.converter.api

public sealed interface MainScreenUiEvent {
    public data object OnDebugPanelClick : MainScreenUiEvent
    public data object OnSettingsClick : MainScreenUiEvent
}
