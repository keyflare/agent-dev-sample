package com.keyflare.common.utils

import android.util.Log

private const val APP_LOGGER_TAG = "AppLogger"

internal actual fun printAppLog(
    level: AppLogLevel,
    message: String,
    exception: Throwable?,
) {
    when (level) {
        AppLogLevel.NEUTRAL -> Log.d(APP_LOGGER_TAG, message)
        AppLogLevel.WARNING -> Log.w(APP_LOGGER_TAG, message, exception)
        AppLogLevel.ERROR -> Log.e(APP_LOGGER_TAG, message, exception)
    }
}
