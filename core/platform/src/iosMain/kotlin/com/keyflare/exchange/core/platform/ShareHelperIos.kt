package com.keyflare.exchange.core.platform

import com.keyflare.exchange.core.analytics.A
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

public class ShareHelperIos : ShareHelper {

    override fun startShareSheet(text: String): Boolean {
        MainScope().launch {
            startShareSheetInternal(text)
        }
        return true
    }

    private fun startShareSheetInternal(text: String) {
        val presenter = UIApplication.sharedApplication.topViewController()
        if (presenter == null) {
            A.reportError(message = "Cannot start share sheet: presenter is unavailable")
            return
        }

        val activityViewController = UIActivityViewController(
            activityItems = listOf(text),
            applicationActivities = null,
        )
        presenter.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null,
        )
    }
}

private fun UIApplication.topViewController(): UIViewController? {
    return keyWindow?.rootViewController?.topMostPresented()
}

private fun UIViewController.topMostPresented(): UIViewController {
    return presentedViewController?.topMostPresented() ?: this
}
