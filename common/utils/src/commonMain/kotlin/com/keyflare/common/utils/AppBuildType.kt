package com.keyflare.common.utils

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
enum class AppBuildType {
    DEBUG,
    DEV,
    @ObjCName(swiftName = "production")
    RELEASE,
}

val AppBuildType.isDebugLike: Boolean
    get() = this == AppBuildType.DEBUG || this == AppBuildType.DEV
