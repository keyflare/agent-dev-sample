package com.keyflare.exchange.feature.converter.api

import androidx.compose.runtime.Immutable

@Immutable
public data class MainScreenViewState(
    val query: String,
    val suggestions: List<HeroSearchSuggestionViewState>,
    val isSearching: Boolean,
    val searchError: String?,
)

@Immutable
public data class HeroSearchSuggestionViewState(
    val id: Int,
    val name: String,
)
