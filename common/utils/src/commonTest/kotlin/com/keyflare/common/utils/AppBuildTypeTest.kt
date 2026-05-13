package com.keyflare.common.utils

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AppBuildTypeTest {

    @Test
    fun debug_like_builds_include_debug_and_dev_only() {
        assertTrue(AppBuildType.DEBUG.isDebugLike)
        assertTrue(AppBuildType.DEV.isDebugLike)
        assertFalse(AppBuildType.RELEASE.isDebugLike)
    }
}
