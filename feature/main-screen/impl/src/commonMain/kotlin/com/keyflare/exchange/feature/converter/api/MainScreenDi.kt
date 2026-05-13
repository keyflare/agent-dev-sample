package com.keyflare.exchange.feature.converter.api

import com.keyflare.common.utils.AppBuildType
import com.keyflare.exchange.core.comicvine.ComicVineCharacterSearchResult
import com.keyflare.exchange.core.comicvine.ComicVineCharactersRepository

public class MainScreenDi(
    internal val buildType: AppBuildType,
    internal val comicVineCharactersRepository: ComicVineCharactersRepository,
) {
    public constructor(
        buildType: AppBuildType,
    ) : this(
        buildType = buildType,
        comicVineCharactersRepository = NoOpComicVineCharactersRepository,
    )
}

private object NoOpComicVineCharactersRepository : ComicVineCharactersRepository {
    override suspend fun searchCharacters(query: String): List<ComicVineCharacterSearchResult> =
        emptyList()
}
