package com.keyflare.exchange.core.comicvine

public interface ComicVineCharactersRepository {

    public suspend fun searchCharacters(query: String): List<ComicVineCharacterSearchResult>
}

public class ComicVineException(
    message: String,
) : RuntimeException(message)
