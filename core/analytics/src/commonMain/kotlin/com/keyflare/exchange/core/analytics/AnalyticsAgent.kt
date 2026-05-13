package com.keyflare.exchange.core.analytics

public interface AnalyticsAgent {
    public fun reportEvent(
        event: String,
        data: Map<String?, Any?>? = null,
    )

    public fun reportError(
        message: String,
        error: Throwable? = null,
    )
}

public object NoOpAnalyticsAgent : AnalyticsAgent {
    override fun reportEvent(
        event: String,
        data: Map<String?, Any?>?,
    ): Unit = Unit

    override fun reportError(
        message: String,
        error: Throwable?,
    ): Unit = Unit
}
