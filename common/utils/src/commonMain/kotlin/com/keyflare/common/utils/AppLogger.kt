package com.keyflare.common.utils

object AppLogger {
    private var isEnabled: Boolean = false

    fun initialize(buildType: AppBuildType) {
        isEnabled = buildType.isDebugLike
    }

    fun neutral(message: String) {
        log(AppLogLevel.NEUTRAL, message, exception = null)
    }

    fun warning(message: String, exception: Throwable? = null) {
        log(AppLogLevel.WARNING, message, exception)
    }

    fun error(message: String, exception: Throwable) {
        log(AppLogLevel.ERROR, message, exception)
    }

    private fun log(
        level: AppLogLevel,
        message: String,
        exception: Throwable?,
    ) {
        if (!isEnabled) return

        appLogPrinter(level, level.format(message), exception)
    }
}

internal enum class AppLogLevel(private val symbol: String) {
    NEUTRAL("🟢"),
    WARNING("🟡"),
    ERROR("🔴");

    fun format(message: String): String = "[AppLogger]: $symbol $message"
}

internal var appLogPrinter: (
    level: AppLogLevel,
    message: String,
    exception: Throwable?,
) -> Unit = ::printAppLog

internal fun resetAppLoggerForTest() {
    AppLogger.initialize(AppBuildType.RELEASE)
    appLogPrinter = ::printAppLog
}

internal expect fun printAppLog(
    level: AppLogLevel,
    message: String,
    exception: Throwable?,
)
