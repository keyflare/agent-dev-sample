package com.keyflare.exchange.core.platform

import android.content.ActivityNotFoundException
import android.content.Intent
import com.keyflare.exchange.core.analytics.A

public class ShareHelperAndroid(
    private val activityProvider: ActivityProvider,
) : ShareHelper {

    override fun startShareSheet(text: String): Boolean {
        val activity = activityProvider.getCurrentActivity()
        if (activity == null) {
            A.reportError(message = "Cannot start share sheet: activity is unavailable")
            return false
        }

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)

        return runCatching { activity.startActivity(shareIntent) }.fold(
            onSuccess = { true },
            onFailure = { error ->
                if (error is ActivityNotFoundException || error is SecurityException) {
                    A.reportError(message = "Cannot start share sheet", error = error)
                    false
                } else {
                    throw error
                }
            },
        )
    }
}
