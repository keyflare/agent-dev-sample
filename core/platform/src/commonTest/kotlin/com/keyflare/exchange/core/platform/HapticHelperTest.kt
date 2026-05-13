package com.keyflare.exchange.core.platform

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HapticHelperTest {

    @Test
    fun observesEnabledStateAndForwardsFeedbackCalls() {
        val enabled = MutableStateFlow(false)
        val delegate = RecordingHapticDelegate()
        val scope = TestScope()

        val helper = HapticHelper(
            hapticEnabledFlow = enabled,
            hapticDelegate = delegate,
            coroutineScope = scope,
        )
        scope.runCurrent()

        assertEquals(listOf(false), delegate.enabledValues)

        enabled.value = true
        scope.runCurrent()

        helper.levelComplete()
        helper.keyPress()
        helper.tilePlaced()

        assertEquals(listOf(false, true), delegate.enabledValues)
        assertEquals(
            listOf("levelComplete", "keyPress", "tilePlaced"),
            delegate.feedbackCalls,
        )
    }
}

private class RecordingHapticDelegate : HapticDelegate {
    val enabledValues = mutableListOf<Boolean>()
    val feedbackCalls = mutableListOf<String>()

    override fun setEnabled(enabled: Boolean) {
        enabledValues += enabled
    }

    override fun levelComplete() {
        feedbackCalls += "levelComplete"
    }

    override fun keyPress() {
        feedbackCalls += "keyPress"
    }

    override fun tilePlaced() {
        feedbackCalls += "tilePlaced"
    }
}
