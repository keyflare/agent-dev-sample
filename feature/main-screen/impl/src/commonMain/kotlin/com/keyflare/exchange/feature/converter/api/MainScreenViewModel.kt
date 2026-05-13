package com.keyflare.exchange.feature.converter.api

import com.keyflare.common.utils.isDebugLike
import com.keyflare.common.utils.mapState
import com.keyflare.exchange.core.analytics.A
import com.keyflare.exchange.core.navigationtools.ViewModel
import com.keyflare.exchange.feature.converter.MainScreenArgs
import com.keyflare.exchange.feature.converter.internal.MainScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class MainScreenViewModel internal constructor(
    private val args: MainScreenArgs,
    private val navigator: MainScreenNavigator,
    private val di: MainScreenDi,
) : ViewModel<MainScreenViewState, MainScreenUiEvent>() {

    private val debugPanelAvailable = di.buildType.isDebugLike

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
        }
    }

    private fun MainScreenState.toViewState(): MainScreenViewState {
        return MainScreenViewState(
            stub = stub,
        )
    }

    private companion object {
        val INITIAL_STATE = MainScreenState(
            stub = "Main Screen Stub"
        )

        const val RATES_UPDATE_PERIOD_MINUTES = 7
        const val CURRENCY_CHOOSER_REQUEST_ID = "mainScreen"
    }
}
