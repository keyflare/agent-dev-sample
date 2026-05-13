package com.keyflare.exchange.feature.debugpanel.internal.screen.start

import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class StartScreenStateTest {

    @Test
    fun toViewState_showsUiGroup_whenEnabled() {
        val screen = assertIs<UtilityScreenViewState.Screen.General>(
            StartScreenState(showUiGroup = true).toViewState(onUiEvent = {}),
        )

        assertEquals(
            listOf("UI", "General"),
            screen.nodes.map { (it as UtilityScreenViewState.ScreenNode.Group).title },
        )
    }

    @Test
    fun toViewState_hidesUiGroup_whenDisabled() {
        val screen = assertIs<UtilityScreenViewState.Screen.General>(
            StartScreenState(showUiGroup = false).toViewState(onUiEvent = {}),
        )

        assertEquals(
            listOf("General"),
            screen.nodes.map { (it as UtilityScreenViewState.ScreenNode.Group).title },
        )
    }
}
