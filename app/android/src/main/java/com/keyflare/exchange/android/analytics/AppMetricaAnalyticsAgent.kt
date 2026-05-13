package com.keyflare.exchange.android.analytics

import android.app.Application
import com.keyflare.exchange.core.analytics.AnalyticsAgent
import com.keyflare.exchange.core.analytics.NoOpAnalyticsAgent
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

internal class AppMetricaAnalyticsAgent(
    private val transport: AppMetricaTransport,
    private val flushOnReport: Boolean,
) : AnalyticsAgent {

    override fun reportEvent(
        event: String,
        data: Map<String?, Any?>?,
    ) {
        transport.reportEvent(event, data)
        flushIfNeeded()
    }

    override fun reportError(
        message: String,
        error: Throwable?,
    ) {
        transport.reportError(message, error)
        flushIfNeeded()
    }

    private fun flushIfNeeded() {
        if (flushOnReport) {
            transport.flushEventsBuffer()
        }
    }

    companion object {
        fun create(
            application: Application,
            apiKey: String,
            logsEnabled: Boolean,
        ): AnalyticsAgent {
            if (apiKey.isBlank()) {
                return NoOpAnalyticsAgent
            }

            val builder = AppMetricaConfig.newConfigBuilder(apiKey)
                .withAdvIdentifiersTracking(false)
            if (logsEnabled) {
                builder.withLogs()
            }
            AppMetrica.activate(application, builder.build())
            AppMetrica.enableActivityAutoTracking(application)
            return AppMetricaAnalyticsAgent(
                transport = StaticAppMetricaTransport,
                flushOnReport = logsEnabled,
            )
        }
    }
}

internal interface AppMetricaTransport {
    fun reportEvent(
        event: String,
        data: Map<String?, Any?>?,
    )

    fun reportError(
        message: String,
        error: Throwable?,
    )

    fun flushEventsBuffer()
}

private object StaticAppMetricaTransport : AppMetricaTransport {
    override fun reportEvent(
        event: String,
        data: Map<String?, Any?>?,
    ) {
        AppMetrica.reportEvent(event, data)
    }

    override fun reportError(
        message: String,
        error: Throwable?,
    ) {
        AppMetrica.reportError(message, error)
    }

    override fun flushEventsBuffer() {
        AppMetrica.sendEventsBuffer()
    }
}
