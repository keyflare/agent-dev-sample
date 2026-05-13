package com.keyflare.exchange.core.platform

import android.view.HapticFeedbackConstants
import android.view.View

public class HapticDelegateAndroid(
    private val activityProvider: ActivityProvider,
) : HapticDelegate {

    private val view: View?
        get() = activityProvider
            .getCurrentActivity()
            ?.findViewById(android.R.id.content)

    private var enabled = false

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    override fun levelComplete() {
        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    override fun keyPress() {
        performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }

    override fun tilePlaced() {
        performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
    }

    private fun performHapticFeedback(mode: Int) {
        if (!enabled) return
        view?.performHapticFeedback(mode)
    }
}
