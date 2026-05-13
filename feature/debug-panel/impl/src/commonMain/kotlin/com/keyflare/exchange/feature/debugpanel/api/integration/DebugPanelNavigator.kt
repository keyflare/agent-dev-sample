package com.keyflare.exchange.feature.debugpanel.api.integration

import com.keyflare.exchange.feature.debugpanel.DebugPanelArgs

public interface DebugPanelNavigator {
    public fun navigateBack()
    public fun navigateToDebugPanelScreen(args: DebugPanelArgs)
    public fun openNetworkLogs()
}
