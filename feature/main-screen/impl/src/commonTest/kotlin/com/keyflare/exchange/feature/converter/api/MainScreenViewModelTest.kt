package com.keyflare.exchange.feature.converter.api

import com.keyflare.common.utils.AppBuildType
import com.keyflare.exchange.core.comicvine.ComicVineCharacterSearchResult
import com.keyflare.exchange.core.comicvine.ComicVineCharactersRepository
import com.keyflare.exchange.feature.converter.MainScreenArgs
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun query_change_waits_500ms_before_searching() = runTest(dispatcher) {
        val repository = FakeComicVineCharactersRepository()
        val search = repository.enqueueSuccess(character(id = 1, name = "Batman"))
        val viewModel = createViewModel(repository = repository)
        runCurrent()

        viewModel.onUiEvent(MainScreenUiEvent.OnSearchQueryChanged("bat"))
        runCurrent()

        assertEquals("bat", viewModel.viewState.value.query)
        assertFalse(viewModel.viewState.value.isSearching)
        assertEquals(emptyList(), repository.queries)

        advanceTimeBy(499)
        runCurrent()

        assertEquals(emptyList(), repository.queries)

        advanceTimeBy(1)
        runCurrent()

        assertEquals(listOf("bat"), repository.queries)
        assertTrue(viewModel.viewState.value.isSearching)

        search.complete(Unit)
        runCurrent()

        assertFalse(viewModel.viewState.value.isSearching)
        assertEquals(
            listOf(HeroSearchSuggestionViewState(id = 1, name = "Batman")),
            viewModel.viewState.value.suggestions,
        )
    }

    @Test
    fun search_click_runs_immediately() = runTest(dispatcher) {
        val repository = FakeComicVineCharactersRepository()
        val search = repository.enqueueSuccess(character(id = 2, name = "Storm"))
        val viewModel = createViewModel(repository = repository)
        runCurrent()

        viewModel.onUiEvent(MainScreenUiEvent.OnSearchQueryChanged("storm"))
        runCurrent()
        viewModel.onUiEvent(MainScreenUiEvent.OnSearchClick)
        runCurrent()

        assertEquals(listOf("storm"), repository.queries)
        assertTrue(viewModel.viewState.value.isSearching)

        search.complete(Unit)
        runCurrent()

        assertFalse(viewModel.viewState.value.isSearching)
        assertEquals(
            listOf(HeroSearchSuggestionViewState(id = 2, name = "Storm")),
            viewModel.viewState.value.suggestions,
        )
    }

    @Test
    fun blank_query_clears_suggestions() = runTest(dispatcher) {
        val repository = FakeComicVineCharactersRepository()
        val firstSearch = repository.enqueueSuccess(character(id = 3, name = "Flash"))
        val viewModel = createViewModel(repository = repository)
        runCurrent()

        viewModel.onUiEvent(MainScreenUiEvent.OnSearchQueryChanged("flash"))
        viewModel.onUiEvent(MainScreenUiEvent.OnSearchClick)
        runCurrent()
        firstSearch.complete(Unit)
        runCurrent()

        assertEquals(
            listOf(HeroSearchSuggestionViewState(id = 3, name = "Flash")),
            viewModel.viewState.value.suggestions,
        )

        viewModel.onUiEvent(MainScreenUiEvent.OnSearchQueryChanged(" "))
        runCurrent()

        assertEquals(" ", viewModel.viewState.value.query)
        assertEquals(emptyList(), viewModel.viewState.value.suggestions)
        assertFalse(viewModel.viewState.value.isSearching)
        assertNull(viewModel.viewState.value.searchError)
        assertEquals(listOf("flash"), repository.queries)
    }

    @Test
    fun stale_search_response_is_ignored() = runTest(dispatcher) {
        val repository = FakeComicVineCharactersRepository()
        val firstSearch = repository.enqueueSuccess(character(id = 4, name = "Cyclops"))
        val secondSearch = repository.enqueueSuccess(character(id = 5, name = "Jean Grey"))
        val viewModel = createViewModel(repository = repository)
        runCurrent()

        viewModel.onUiEvent(MainScreenUiEvent.OnSearchQueryChanged("cyclops"))
        viewModel.onUiEvent(MainScreenUiEvent.OnSearchClick)
        runCurrent()
        viewModel.onUiEvent(MainScreenUiEvent.OnSearchQueryChanged("jean"))
        viewModel.onUiEvent(MainScreenUiEvent.OnSearchClick)
        runCurrent()

        assertEquals(listOf("cyclops", "jean"), repository.queries)

        secondSearch.complete(Unit)
        runCurrent()

        assertEquals(
            listOf(HeroSearchSuggestionViewState(id = 5, name = "Jean Grey")),
            viewModel.viewState.value.suggestions,
        )
        assertFalse(viewModel.viewState.value.isSearching)

        firstSearch.complete(Unit)
        runCurrent()

        assertEquals(
            listOf(HeroSearchSuggestionViewState(id = 5, name = "Jean Grey")),
            viewModel.viewState.value.suggestions,
        )
        assertFalse(viewModel.viewState.value.isSearching)
    }

    @Test
    fun failed_search_sets_error_and_clears_loading() = runTest(dispatcher) {
        val repository = FakeComicVineCharactersRepository()
        val firstSearch = repository.enqueueSuccess(character(id = 6, name = "Rogue"))
        val failedSearch = repository.enqueueFailure(IllegalStateException("network"))
        val viewModel = createViewModel(repository = repository)
        runCurrent()

        viewModel.onUiEvent(MainScreenUiEvent.OnSearchQueryChanged("rogue"))
        viewModel.onUiEvent(MainScreenUiEvent.OnSearchClick)
        runCurrent()
        firstSearch.complete(Unit)
        runCurrent()

        viewModel.onUiEvent(MainScreenUiEvent.OnSearchQueryChanged("gambit"))
        viewModel.onUiEvent(MainScreenUiEvent.OnSearchClick)
        runCurrent()
        failedSearch.complete(Unit)
        runCurrent()

        assertEquals(emptyList(), viewModel.viewState.value.suggestions)
        assertFalse(viewModel.viewState.value.isSearching)
        assertEquals("Search failed", viewModel.viewState.value.searchError)
    }

    private fun createViewModel(
        repository: ComicVineCharactersRepository = FakeComicVineCharactersRepository(),
        ioDispatcher: CoroutineDispatcher = dispatcher,
        navigator: MainScreenNavigator = FakeMainScreenNavigator(),
    ): MainScreenViewModel {
        return MainScreenViewModel(
            args = MainScreenArgs,
            navigator = navigator,
            di = MainScreenDi(
                buildType = AppBuildType.DEBUG,
                comicVineCharactersRepository = repository,
                ioDispatcher = ioDispatcher,
            ),
        )
    }

    private class FakeComicVineCharactersRepository : ComicVineCharactersRepository {

        val queries = mutableListOf<String>()
        private val responses = ArrayDeque<Response>()

        fun enqueueSuccess(
            vararg results: ComicVineCharacterSearchResult,
        ): CompletableDeferred<Unit> {
            val deferred = CompletableDeferred<Unit>()
            responses += Response(deferred = deferred, result = Result.success(results.toList()))
            return deferred
        }

        fun enqueueFailure(
            throwable: Throwable,
        ): CompletableDeferred<Unit> {
            val deferred = CompletableDeferred<Unit>()
            responses += Response(deferred = deferred, result = Result.failure(throwable))
            return deferred
        }

        override suspend fun searchCharacters(query: String): List<ComicVineCharacterSearchResult> {
            queries += query
            val response = responses.removeFirst()
            response.deferred.await()
            return response.result.getOrThrow()
        }

        private data class Response(
            val deferred: CompletableDeferred<Unit>,
            val result: Result<List<ComicVineCharacterSearchResult>>,
        )
    }

    private class FakeMainScreenNavigator : MainScreenNavigator {
        override fun navigateToDebugPanel() = Unit
        override fun navigateToSettings() = Unit
    }
}

private fun character(
    id: Int,
    name: String,
): ComicVineCharacterSearchResult {
    return ComicVineCharacterSearchResult(
        id = id,
        name = name,
        apiDetailUrl = "https://example.com/$id",
    )
}
