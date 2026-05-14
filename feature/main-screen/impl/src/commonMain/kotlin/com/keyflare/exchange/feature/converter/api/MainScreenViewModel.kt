package com.keyflare.exchange.feature.converter.api

import com.keyflare.common.utils.isDebugLike
import com.keyflare.common.utils.mapState
import com.keyflare.exchange.core.analytics.A
import com.keyflare.exchange.core.comicvine.ComicVineCharacterSearchResult
import com.keyflare.exchange.core.comicvine.ComicVineException
import com.keyflare.exchange.core.navigationtools.ViewModel
import com.keyflare.exchange.feature.converter.MainScreenArgs
import com.keyflare.exchange.feature.converter.internal.MainScreenState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

public class MainScreenViewModel internal constructor(
    @Suppress("UNUSED_PARAMETER")
    private val args: MainScreenArgs,
    private val navigator: MainScreenNavigator,
    private val di: MainScreenDi,
) : ViewModel<MainScreenViewState, MainScreenUiEvent>() {

    private val debugPanelAvailable = di.buildType.isDebugLike
    private var searchDebounceJob: Job? = null
    private var searchGeneration: Long = 0

    private val state = MutableStateFlow(INITIAL_STATE)
    override val viewState: StateFlow<MainScreenViewState> =
        state.mapState(scope = viewModelScope) { it.toViewState() }

    init {
        A.MainScreen.trackOpened()
    }

    override fun onUiEvent(event: MainScreenUiEvent) {
        when (event) {
            MainScreenUiEvent.OnSettingsClick -> {
                A.MainScreen.trackSettingsClicked()
                navigator.navigateToSettings()
            }

            MainScreenUiEvent.OnDebugPanelClick -> {
                if (!debugPanelAvailable) return

                A.MainScreen.trackDebugPanelClicked()
                navigator.navigateToDebugPanel()
            }

            MainScreenUiEvent.OnSearchClick -> onSearchClick()
            is MainScreenUiEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.query)
            is MainScreenUiEvent.OnSuggestionClick -> Unit
        }
    }

    private fun onSearchQueryChanged(query: String) {
        searchDebounceJob?.cancel()
        searchGeneration++

        if (query.isBlank()) {
            state.value = state.value.copy(
                query = query,
                suggestions = emptyList(),
                isSearching = false,
                searchError = null,
            )
            return
        }

        state.value = state.value.copy(
            query = query,
            searchError = null,
        )
        searchDebounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            search(query)
        }
    }

    private fun onSearchClick() {
        searchDebounceJob?.cancel()
        val query = state.value.query
        if (query.isBlank()) return

        search(query)
    }

    private fun search(query: String) {
        val generation = ++searchGeneration

        state.value = state.value.copy(
            isSearching = true,
            searchError = null,
        )

        viewModelScope.launch {
            val result = runCatching {
                withContext(di.ioDispatcher) {
                    di.comicVineCharactersRepository.searchCharacters(query)
                }
            }
            if (result.exceptionOrNull() is CancellationException) {
                throw result.exceptionOrNull() as CancellationException
            }
            if (generation != searchGeneration || state.value.query != query) return@launch

            state.value = result.fold(
                onSuccess = { suggestions ->
                    state.value.copy(
                        suggestions = suggestions,
                        isSearching = false,
                        searchError = null,
                    )
                },
                onFailure = {
                    state.value.copy(
                        suggestions = emptyList(),
                        isSearching = false,
                        searchError = it.toSearchErrorMessage(),
                    )
                },
            )
        }
    }

    private fun MainScreenState.toViewState(): MainScreenViewState {
        return MainScreenViewState(
            query = query,
            suggestions = suggestions.map { it.toViewState() },
            isSearching = isSearching,
            searchError = searchError,
        )
    }

    private fun ComicVineCharacterSearchResult.toViewState(): HeroSearchSuggestionViewState {
        return HeroSearchSuggestionViewState(
            id = id,
            name = name,
        )
    }

    private fun Throwable.toSearchErrorMessage(): String {
        return if (this is ComicVineException && !message.isNullOrBlank()) {
            message.orEmpty()
        } else {
            SEARCH_ERROR_MESSAGE
        }
    }

    private companion object {
        val INITIAL_STATE = MainScreenState(
            query = "",
            suggestions = emptyList(),
            isSearching = false,
            searchError = null,
        )

        const val SEARCH_DEBOUNCE_MS = 500L
        const val SEARCH_ERROR_MESSAGE = "Search failed"
    }
}
