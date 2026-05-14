package com.keyflare.exchange.feature.converter.api

public sealed interface MainScreenUiEvent {
    public data object OnDebugPanelClick : MainScreenUiEvent
    public data object OnSearchClick : MainScreenUiEvent
    public data class OnSearchQueryChanged(val query: String) : MainScreenUiEvent
    public data class OnSuggestionClick(val id: Int) : MainScreenUiEvent
    public data object OnSettingsClick : MainScreenUiEvent
}
