package com.keyflare.common.datastore

import android.content.Context

actual class DataStorePlatform(val context: Context)

internal actual fun producePath(
    filename: String,
    platform: DataStorePlatform,
    useExternalFilesDir: Boolean,
): String {
    return if (useExternalFilesDir) {
        platform.context.getExternalFilesDir(null)!!.resolve(filename).absolutePath
    } else {
        platform.context.filesDir.resolve(filename).absolutePath
    }
}
