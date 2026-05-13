package com.keyflare.exchange.core.platform

public data class PlatformServices(
    val shareHelper: ShareHelper = NoOpShareHelper,
    val hapticSystemSettingsProvider: HapticSystemSettingsProvider = DefaultHapticSystemSettingsProvider,
    val hapticDelegate: HapticDelegate = NoOpHapticDelegate,
    val urlOpener: UrlOpener = NoOpUrlOpener,
    val emailHelper: EmailHelper = NoOpEmailHelper,
)

public object NoOpShareHelper : ShareHelper {
    override fun startShareSheet(text: String): Boolean = false
}

public object DefaultHapticSystemSettingsProvider : HapticSystemSettingsProvider {
    override val enabledInSystem: Boolean = true
}

public object NoOpHapticDelegate : HapticDelegate {
    override fun setEnabled(enabled: Boolean): Unit = Unit
    override fun levelComplete(): Unit = Unit
    override fun keyPress(): Unit = Unit
    override fun tilePlaced(): Unit = Unit
}

public object NoOpUrlOpener : UrlOpener {
    override fun openUrl(url: String): Boolean = false
}

public object NoOpEmailHelper : EmailHelper {
    override fun writeEmail(
        contactEmail: String,
        subject: String,
        body: String?,
    ): Boolean = false
}
