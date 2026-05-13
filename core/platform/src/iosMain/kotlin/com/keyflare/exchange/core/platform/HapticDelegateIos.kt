package com.keyflare.exchange.core.platform

import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackType

public class HapticDelegateIos : HapticDelegate {
    private var enabled = false

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    override fun levelComplete() {
        if (!enabled) return
        UINotificationFeedbackGenerator()
            .notificationOccurred(UINotificationFeedbackType.UINotificationFeedbackTypeSuccess)
    }

    override fun keyPress() {
        impact(UIImpactFeedbackStyle.UIImpactFeedbackStyleLight)
    }

    override fun tilePlaced() {
        impact(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
    }

    private fun impact(style: UIImpactFeedbackStyle) {
        if (!enabled) return
        UIImpactFeedbackGenerator(style = style).impactOccurred()
    }
}
