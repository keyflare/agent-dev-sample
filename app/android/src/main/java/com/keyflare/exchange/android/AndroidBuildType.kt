package com.keyflare.exchange.android

import com.keyflare.common.utils.AppBuildType

internal fun String.toAppBuildType(): AppBuildType = when (this) {
    "debug" -> AppBuildType.DEBUG
    "dev" -> AppBuildType.DEV
    "release" -> AppBuildType.RELEASE
    else -> error("Unsupported Android build type: $this")
}
