package com.keyflare.exchange.android

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import com.keyflare.common.datastore.DataStorePlatform
import com.keyflare.common.utils.isDebugLike
import com.keyflare.exchange.api.ExchangePlatformDependencies
import com.keyflare.exchange.android.analytics.AppMetricaAnalyticsAgent
import com.keyflare.exchange.core.platform.AndroidActivityProvider
import com.keyflare.exchange.core.platform.EmailHelperAndroid
import com.keyflare.exchange.core.platform.HapticDelegateAndroid
import com.keyflare.exchange.core.platform.HapticSystemSettingsProviderAndroid
import com.keyflare.exchange.core.platform.PlatformServices
import com.keyflare.exchange.core.platform.ShareHelperAndroid
import com.keyflare.exchange.core.platform.UrlOpenerAndroid

class AndroidApp : Application() {

    private val activityProvider by lazy { AndroidActivityProvider(this) }

    override fun onCreate() {
        super.onCreate()
        setupMainProcess()
    }

    private fun setupMainProcess() {
        val buildType = BuildConfig.BUILD_TYPE.toAppBuildType()
        val analyticsAgent = AppMetricaAnalyticsAgent.create(
            application = this,
            apiKey = BuildConfig.APPMETRICA_API_KEY,
            logsEnabled = buildType.isDebugLike,
        )
        ExchangeAppHolder.initialize(
            platformDependencies = ExchangePlatformDependencies(
                dataStorePlatform = DataStorePlatform(context = this),
                buildType = buildType,
                analyticsAgent = analyticsAgent,
                platformServices = PlatformServices(
                    shareHelper = ShareHelperAndroid(activityProvider = activityProvider),
                    hapticSystemSettingsProvider = HapticSystemSettingsProviderAndroid(context = this),
                    hapticDelegate = HapticDelegateAndroid(activityProvider = activityProvider),
                    urlOpener = UrlOpenerAndroid(context = this),
                    emailHelper = EmailHelperAndroid(activityProvider = activityProvider),
                ),
                openNetworkLogs = ::openChucker,
            )
        )
    }

    private fun openChucker() {
        val intent = Intent().apply {
            component = ComponentName(
                this@AndroidApp,
                "com.chuckerteam.chucker.internal.ui.MainActivity"
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}
