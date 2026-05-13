package com.keyflare.exchange.feature.settings.api

import com.keyflare.common.datastore.DataStorePlatform
import com.keyflare.exchange.core.platform.EmailHelper
import com.keyflare.exchange.core.platform.NoOpEmailHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

public class SettingsDi private constructor(
    dataStorePlatform: DataStorePlatform?,
    themeStore: AppThemeStore?,
    internal val ioDispatcher: CoroutineDispatcher,
    internal val emailHelper: EmailHelper,
) {
    public constructor(
        dataStorePlatform: DataStorePlatform,
        ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
        emailHelper: EmailHelper = NoOpEmailHelper,
    ) : this(
        dataStorePlatform = dataStorePlatform,
        themeStore = null,
        ioDispatcher = ioDispatcher,
        emailHelper = emailHelper,
    )

    public constructor(
        themeStore: AppThemeStore,
        ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
        emailHelper: EmailHelper = NoOpEmailHelper,
    ) : this(
        dataStorePlatform = null,
        themeStore = themeStore,
        ioDispatcher = ioDispatcher,
        emailHelper = emailHelper,
    )

    public val themeStore: AppThemeStore =
        themeStore ?: AppThemeStore.create(
            dataStorePlatform = requireNotNull(dataStorePlatform) {
                "dataStorePlatform is required when themeStore is not provided"
            },
        )
}
