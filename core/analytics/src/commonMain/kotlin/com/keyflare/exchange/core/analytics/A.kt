package com.keyflare.exchange.core.analytics

public object A {
    private var agent: AnalyticsAgent = NoOpAnalyticsAgent

    public fun initialize(agent: AnalyticsAgent) {
        this.agent = agent
    }

    public object MainScreen {
        public fun trackOpened() {
            reportEvent("mainScreen.opened")
        }

        public fun trackSettingsClicked() {
            reportEvent("mainScreen.settings.clicked")
        }

        public fun trackDebugPanelClicked() {
            reportEvent("mainScreen.debugPanel.clicked")
        }
    }

    public fun reportError(
        message: String,
        error: Throwable? = null,
    ) {
        agent.reportError(message, error)
    }

    private fun reportEvent(
        event: String,
        data: Map<String?, Any?>? = null,
    ) {
        agent.reportEvent(event, data)
    }
}
