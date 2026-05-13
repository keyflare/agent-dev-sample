package com.keyflare.exchange.core.platform

import com.keyflare.exchange.core.analytics.A
import platform.Foundation.NSLog
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

public class UrlOpenerIos : UrlOpener {
    override fun openUrl(url: String): Boolean {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl == null) {
            A.reportError(message = "Invalid URL: $url")
            NSLog("Invalid URL: $url")
            return false
        }

        val application = UIApplication.sharedApplication
        if (!application.canOpenURL(nsUrl)) {
            A.reportError(message = "Cannot open URL: $url")
            NSLog("Cannot open URL: $url")
            return false
        }

        application.openURL(
            url = nsUrl,
            options = emptyMap<Any?, Any?>(),
            completionHandler = null,
        )
        return true
    }
}
