package com.keyflare.exchange.core.comicvine

import com.keyflare.exchange.core.comicvine.internal.ComicVineSearchResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

public class ComicVineCharactersRepositoryImpl(
    private val httpClient: HttpClient,
    private val apiKey: String,
) : ComicVineCharactersRepository {

    override public suspend fun searchCharacters(query: String): List<ComicVineCharacterSearchResult> {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isBlank()) {
            return emptyList()
        }

        val trimmedApiKey = apiKey.trim()
        if (trimmedApiKey.isBlank()) {
            throw ComicVineException("Comic Vine API key is missing")
        }

        val response = httpClient.get("https://comicvine.gamespot.com/api/search/") {
            parameter("api_key", trimmedApiKey)
            parameter("format", "json")
            parameter("query", trimmedQuery)
            parameter("resources", "character")
            parameter("limit", "10")
            parameter("field_list", "id,name,api_detail_url,resource_type")
        }.body<ComicVineSearchResponseDto>()

        if (response.statusCode != SUCCESS_STATUS_CODE) {
            throw ComicVineException(response.error)
        }

        return response.results
            .filter { result -> result.resourceType == CHARACTER_RESOURCE_TYPE }
            .map { result ->
                ComicVineCharacterSearchResult(
                    id = result.id,
                    name = result.name,
                    apiDetailUrl = result.apiDetailUrl,
                )
            }
    }

    private companion object {
        private const val SUCCESS_STATUS_CODE = 1
        private const val CHARACTER_RESOURCE_TYPE = "character"
    }
}
