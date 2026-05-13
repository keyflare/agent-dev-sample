package com.keyflare.exchange.core.utilityscreen.api

public interface UtilityScreenUiEvent {
    public data class OnNodeClick(val nodeId: String) : UtilityScreenUiEvent
    public data object OnBack : UtilityScreenUiEvent
}
