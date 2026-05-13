package com.keyflare.exchange.feature.settings.internal

import androidx.datastore.preferences.core.stringPreferencesKey
import com.keyflare.common.datastore.DataStorePlatform
import com.keyflare.common.datastore.createDataStore
import com.keyflare.common.datastore.dataStoreSyncProperty
import com.keyflare.exchange.feature.settings.api.AppThemeMode
import com.keyflare.exchange.feature.settings.api.AppThemeStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal fun createAppThemeStore(
    dataStorePlatform: DataStorePlatform,
): AppThemeStore {
    return DataStoreAppThemeStore(dataStorePlatform)
}

private class DataStoreAppThemeStore(
    dataStorePlatform: DataStorePlatform,
) : AppThemeStore {

    private val dataStore = createDataStore(
        platform = dataStorePlatform,
        filename = DATA_STORE_FILE_NAME,
    )

    private var storedThemeMode: String by dataStoreSyncProperty(
        dataStore = dataStore,
        key = stringPreferencesKey(THEME_MODE_KEY),
        defaultValue = AppThemeMode.System.name,
        enableCache = true,
    )

    private val state = MutableStateFlow(storedThemeMode.toAppThemeMode())

    override val themeMode: StateFlow<AppThemeMode> = state.asStateFlow()

    override suspend fun setThemeMode(themeMode: AppThemeMode) {
        if (state.value == themeMode) return

        storedThemeMode = themeMode.name
        state.value = themeMode
    }

    private fun String.toAppThemeMode(): AppThemeMode {
        return AppThemeMode.entries.firstOrNull { it.name == this } ?: AppThemeMode.System
    }

    private companion object {
        private const val DATA_STORE_FILE_NAME = "AppThemeStore.preferences_pb"
        private const val THEME_MODE_KEY = "THEME_MODE_KEY"
    }
}
