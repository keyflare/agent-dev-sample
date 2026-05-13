package com.keyflare.exchange.core.platform

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.keyflare.exchange.core.analytics.A

public class UrlOpenerAndroid(
    private val context: Context,
) : UrlOpener {

    override fun openUrl(url: String): Boolean {
        return runCatching {
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                .let(context::startActivity)
        }.fold(
            onSuccess = { true },
            onFailure = { error ->
                if (error is ActivityNotFoundException || error is SecurityException) {
                    A.reportError(message = "Cannot open URL: $url", error = error)
                    false
                } else {
                    throw error
                }
            },
        )
    }
}
