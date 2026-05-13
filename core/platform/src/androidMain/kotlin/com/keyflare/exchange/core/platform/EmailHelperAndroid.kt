package com.keyflare.exchange.core.platform

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.keyflare.exchange.core.analytics.A

public class EmailHelperAndroid(
    private val activityProvider: ActivityProvider,
) : EmailHelper {

    override fun writeEmail(
        contactEmail: String,
        subject: String,
        body: String?,
    ): Boolean {
        val activity = activityProvider.getCurrentActivity()
        if (activity == null) {
            A.reportError(message = "Cannot write email: activity is unavailable")
            return false
        }

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(contactEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            body?.let { putExtra(Intent.EXTRA_TEXT, it) }
        }

        return runCatching {
            activity.startActivity(Intent.createChooser(intent, null))
        }.fold(
            onSuccess = { true },
            onFailure = { error ->
                if (error is ActivityNotFoundException || error is SecurityException) {
                    A.reportError(message = "Cannot write email to $contactEmail", error = error)
                    false
                } else {
                    throw error
                }
            },
        )
    }
}
