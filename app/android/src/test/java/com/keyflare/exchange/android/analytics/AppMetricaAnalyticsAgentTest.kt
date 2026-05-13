package com.keyflare.exchange.android.analytics

import kotlin.test.Test
import kotlin.test.assertEquals

class AppMetricaAnalyticsAgentTest {

    @Test
    fun reportEventFlushesEventsBufferWhenFlushOnReportIsEnabled() {
        val transport = FakeAppMetricaTransport()
        val agent = AppMetricaAnalyticsAgent(
            transport = transport,
            flushOnReport = true,
        )

        agent.reportEvent(
            event = "mainScreen.opened",
            data = mapOf("source" to "test"),
        )

        assertEquals(
            expected = listOf(
                "event:mainScreen.opened:{source=test}",
                "flush",
            ),
            actual = transport.calls,
        )
    }

    @Test
    fun reportEventDoesNotFlushEventsBufferWhenFlushOnReportIsDisabled() {
        val transport = FakeAppMetricaTransport()
        val agent = AppMetricaAnalyticsAgent(
            transport = transport,
            flushOnReport = false,
        )

        agent.reportEvent(
            event = "mainScreen.opened",
            data = null,
        )

        assertEquals(
            expected = listOf("event:mainScreen.opened:null"),
            actual = transport.calls,
        )
    }

    @Test
    fun reportErrorFlushesEventsBufferWhenFlushOnReportIsEnabled() {
        val transport = FakeAppMetricaTransport()
        val agent = AppMetricaAnalyticsAgent(
            transport = transport,
            flushOnReport = true,
        )

        agent.reportError(
            message = "Something failed",
            error = IllegalStateException("boom"),
        )

        assertEquals(
            expected = listOf(
                "error:Something failed:IllegalStateException",
                "flush",
            ),
            actual = transport.calls,
        )
    }
}

private class FakeAppMetricaTransport : AppMetricaTransport {
    val calls = mutableListOf<String>()

    override fun reportEvent(
        event: String,
        data: Map<String?, Any?>?,
    ) {
        calls += "event:$event:$data"
    }

    override fun reportError(
        message: String,
        error: Throwable?,
    ) {
        calls += "error:$message:${error?.javaClass?.simpleName}"
    }

    override fun flushEventsBuffer() {
        calls += "flush"
    }
}
