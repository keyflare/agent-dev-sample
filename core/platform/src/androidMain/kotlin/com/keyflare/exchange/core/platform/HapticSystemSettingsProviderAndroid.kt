package com.keyflare.exchange.core.platform

import android.content.Context
import android.provider.Settings

public class HapticSystemSettingsProviderAndroid(
    private val context: Context,
) : HapticSystemSettingsProvider {

    @Suppress("DEPRECATION")
    override val enabledInSystem: Boolean
        get() = Settings.System.getInt(
            context.contentResolver,
            Settings.System.HAPTIC_FEEDBACK_ENABLED,
            1,
        ) == 1
}
