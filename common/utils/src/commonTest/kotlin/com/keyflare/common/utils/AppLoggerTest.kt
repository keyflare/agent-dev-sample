package com.keyflare.common.utils

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class AppLoggerTest {

    private val printedLogs = mutableListOf<PrintedLog>()

    @AfterTest
    fun tearDown() {
        resetAppLoggerForTest()
    }

    @Test
    fun neutral_log_prints_green_message_when_logger_is_enabled() {
        captureLogs()
        AppLogger.initialize(AppBuildType.DEBUG)

        AppLogger.neutral("Some message")

        assertEquals(
            listOf(PrintedLog(AppLogLevel.NEUTRAL, "[AppLogger]: 🟢 Some message", null)),
            printedLogs
        )
    }

    @Test
    fun warning_log_prints_yellow_message_and_keeps_optional_exception() {
        captureLogs()
        AppLogger.initialize(AppBuildType.DEV)
        val exception = IllegalStateException("Bad state")

        AppLogger.warning("Some message", exception)

        assertEquals(AppLogLevel.WARNING, printedLogs.single().level)
        assertEquals("[AppLogger]: 🟡 Some message", printedLogs.single().message)
        assertSame(exception, printedLogs.single().exception)
    }

    @Test
    fun warning_log_allows_missing_exception() {
        captureLogs()
        AppLogger.initialize(AppBuildType.DEBUG)

        AppLogger.warning("Some message")

        assertEquals(
            listOf(PrintedLog(AppLogLevel.WARNING, "[AppLogger]: 🟡 Some message", null)),
            printedLogs
        )
    }

    @Test
    fun error_log_prints_red_message_and_keeps_required_exception() {
        captureLogs()
        AppLogger.initialize(AppBuildType.DEBUG)
        val exception = IllegalArgumentException("Bad argument")

        AppLogger.error("Some message", exception)

        assertEquals(AppLogLevel.ERROR, printedLogs.single().level)
        assertEquals("[AppLogger]: 🔴 Some message", printedLogs.single().message)
        assertSame(exception, printedLogs.single().exception)
    }

    @Test
    fun release_build_disables_logs() {
        captureLogs()
        AppLogger.initialize(AppBuildType.RELEASE)

        AppLogger.neutral("Neutral message")
        AppLogger.warning("Warning message")
        AppLogger.error("Error message", IllegalStateException("Failure"))

        assertTrue(printedLogs.isEmpty())
    }

    @Test
    fun default_platform_logger_accepts_kotlin_string_message() {
        if (platform != Platform.IOS) return

        AppLogger.initialize(AppBuildType.DEBUG)

        AppLogger.neutral("NetworkStatusController.reportBackendUnreachable(requestUrl=http://localhost/quotes)")
    }

    private fun captureLogs() {
        appLogPrinter = { level, message, exception ->
            printedLogs += PrintedLog(level, message, exception)
        }
    }

    private data class PrintedLog(
        val level: AppLogLevel,
        val message: String,
        val exception: Throwable?,
    )
}
