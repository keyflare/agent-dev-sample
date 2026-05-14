package com.keyflare.exchange.feature.converter.internal

import com.keyflare.exchange.core.comicvine.ComicVineCharacterSearchResult

internal data class MainScreenState(
    val query: String,
    val suggestions: List<ComicVineCharacterSearchResult>,
    val isSearching: Boolean,
    val searchError: String?,
)
