package com.keyflare.exchange.feature.converter.api

import com.keyflare.common.utils.AppBuildType
import com.keyflare.exchange.core.comicvine.ComicVineCharactersRepository

public class MainScreenDi(
    internal val buildType: AppBuildType,
    internal val comicVineCharactersRepository: ComicVineCharactersRepository,
)
