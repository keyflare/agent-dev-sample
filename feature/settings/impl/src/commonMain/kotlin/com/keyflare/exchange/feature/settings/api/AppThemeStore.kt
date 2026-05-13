package com.keyflare.exchange.feature.settings.api

import com.keyflare.common.datastore.DataStorePlatform
import com.keyflare.exchange.feature.settings.internal.createAppThemeStore
import kotlinx.coroutines.flow.StateFlow

public interface AppThemeStore {
    public val themeMode: StateFlow<AppThemeMode>

    public suspend fun setThemeMode(themeMode: AppThemeMode)

    public companion object {
        public fun create(dataStorePlatform: DataStorePlatform): AppThemeStore {
            return createAppThemeStore(dataStorePlatform)
        }
    }
}
