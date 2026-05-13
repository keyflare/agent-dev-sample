package com.keyflare.exchange.core.comicvine.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ComicVineSearchResponseDto(
    @SerialName("status_code")
    val statusCode: Int,
    val error: String,
    val results: List<ComicVineSearchResultDto> = emptyList(),
)

@Serializable
internal data class ComicVineSearchResultDto(
    val id: Int,
    val name: String,
    @SerialName("api_detail_url")
    val apiDetailUrl: String,
    @SerialName("resource_type")
    val resourceType: String,
)
