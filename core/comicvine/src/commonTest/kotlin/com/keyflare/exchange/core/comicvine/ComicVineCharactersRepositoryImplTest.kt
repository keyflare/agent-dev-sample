package com.keyflare.exchange.core.comicvine

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ComicVineCharactersRepositoryImplTest {

    @Test
    fun searchCharacters_sendsExpectedQueryParametersAndMapsResults() = runTest {
        val engine = MockEngine { request ->
            assertEquals(HttpMethod.Get, request.method)
            assertEquals("https", request.url.protocol.name)
            assertEquals("comicvine.gamespot.com", request.url.host)
            assertEquals("/api/search/", request.url.encodedPath)
            assertEquals("test-api-key", request.url.parameters["api_key"])
            assertEquals("json", request.url.parameters["format"])
            assertEquals("Batman", request.url.parameters["query"])
            assertEquals("character", request.url.parameters["resources"])
            assertEquals("10", request.url.parameters["limit"])
            assertEquals("id,name,api_detail_url,resource_type", request.url.parameters["field_list"])

            respondJson(
                """
                {
                  "status_code": 1,
                  "error": "OK",
                  "number_of_total_results": 2,
                  "number_of_page_results": 2,
                  "limit": 10,
                  "offset": 0,
                  "results": [
                    {
                      "id": 1699,
                      "name": "Batman",
                      "api_detail_url": "https://comicvine.gamespot.com/api/character/4005-1699/",
                      "resource_type": "character"
                    },
                    {
                      "id": 42,
                      "name": "Batman Volume",
                      "api_detail_url": "https://comicvine.gamespot.com/api/volume/4050-42/",
                      "resource_type": "volume"
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
        val repository = ComicVineCharactersRepositoryImpl(
            httpClient = testHttpClient(engine),
            apiKey = "test-api-key",
        )

        val results = repository.searchCharacters("  Batman  ")

        assertEquals(
            listOf(
                ComicVineCharacterSearchResult(
                    id = 1699,
                    name = "Batman",
                    apiDetailUrl = "https://comicvine.gamespot.com/api/character/4005-1699/",
                ),
            ),
            results,
        )
    }

    @Test
    fun searchCharacters_throwsWhenApiStatusIsNotOk() = runTest {
        val engine = MockEngine {
            respondJson(
                """
                {
                  "status_code": 100,
                  "error": "Invalid API Key",
                  "results": []
                }
                """.trimIndent(),
            )
        }
        val repository = ComicVineCharactersRepositoryImpl(
            httpClient = testHttpClient(engine),
            apiKey = "test-api-key",
        )

        val exception = assertFailsWith<ComicVineException> {
            repository.searchCharacters("Batman")
        }

        assertEquals("Invalid API Key", exception.message)
    }

    @Test
    fun searchCharacters_returnsEmptyListForBlankQuery() = runTest {
        var requestCount = 0
        val engine = MockEngine {
            requestCount += 1
            respondJson("""{"status_code":1,"error":"OK","results":[]}""")
        }
        val repository = ComicVineCharactersRepositoryImpl(
            httpClient = testHttpClient(engine),
            apiKey = "test-api-key",
        )

        val results = repository.searchCharacters("   ")

        assertEquals(emptyList(), results)
        assertEquals(0, requestCount)
    }

    @Test
    fun searchCharacters_throwsWhenApiKeyIsBlank() = runTest {
        var requestCount = 0
        val engine = MockEngine {
            requestCount += 1
            respondJson("""{"status_code":1,"error":"OK","results":[]}""")
        }
        val repository = ComicVineCharactersRepositoryImpl(
            httpClient = testHttpClient(engine),
            apiKey = "   ",
        )

        val exception = assertFailsWith<ComicVineException> {
            repository.searchCharacters("Batman")
        }

        assertEquals("Comic Vine API key is missing", exception.message)
        assertEquals(0, requestCount)
    }

    private fun testHttpClient(engine: MockEngine): HttpClient =
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                )
            }
        }

    private fun MockRequestHandleScope.respondJson(content: String) =
        respond(
            content = content,
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
}
