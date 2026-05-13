package com.keyflare.exchange.core.platform

import com.keyflare.exchange.core.analytics.A
import platform.Foundation.NSLog
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

public class EmailHelperIos : EmailHelper {
    override fun writeEmail(
        contactEmail: String,
        subject: String,
        body: String?,
    ): Boolean {
        val urlString = buildString {
            append("mailto:${contactEmail.encodeUrlQueryValue()}")
            val queryParams = buildList {
                add("subject=${subject.encodeUrlQueryValue()}")
                body?.let { add("body=${it.encodeUrlQueryValue()}") }
            }
            if (queryParams.isNotEmpty()) {
                append("?${queryParams.joinToString("&")}")
            }
        }

        val url = NSURL.URLWithString(urlString)
        val application = UIApplication.sharedApplication
        if (url == null || !application.canOpenURL(url)) {
            A.reportError(message = "Cannot open mailto URL: $urlString")
            NSLog("Cannot open mailto URL: $urlString")
            return false
        }

        application.openURL(
            url = url,
            options = emptyMap<Any?, Any?>(),
            completionHandler = null,
        )
        return true
    }
}

private fun String.encodeUrlQueryValue(): String {
    return encodeToByteArray().joinToString(separator = "") { byte ->
        val value = byte.toInt() and 0xff
        val char = value.toChar()
        if (char.isUrlQueryValueChar()) {
            char.toString()
        } else {
            "%" + value.toString(16).uppercase().padStart(2, '0')
        }
    }
}

private fun Char.isUrlQueryValueChar(): Boolean {
    return this in 'A'..'Z' ||
        this in 'a'..'z' ||
        this in '0'..'9' ||
        this == '-' ||
        this == '.' ||
        this == '_' ||
        this == '~'
}
