package com.keyflare.common.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringCreateWithCString
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFStringEncodingUTF8
import platform.Foundation.NSLog

@OptIn(ExperimentalForeignApi::class)
internal actual fun printAppLog(
    level: AppLogLevel,
    message: String,
    exception: Throwable?,
) {
    val fullMessage = if (exception == null) {
        message
    } else {
        "$message\n$exception"
    }
    val messageRef = CFStringCreateWithCString(
        alloc = kCFAllocatorDefault,
        cStr = fullMessage,
        encoding = kCFStringEncodingUTF8,
    )
    if (messageRef == null) {
        NSLog("Failed to encode log message")
        return
    }

    NSLog("%@", messageRef)
    CFRelease(messageRef)
}
