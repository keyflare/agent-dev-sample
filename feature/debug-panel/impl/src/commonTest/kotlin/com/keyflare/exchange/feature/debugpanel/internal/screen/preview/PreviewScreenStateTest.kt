package com.keyflare.exchange.feature.debugpanel.internal.screen.preview

import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.feature.debugpanel.api.screen.preview.ComponentsPreviewViewState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PreviewScreenStateTest {

    @Test
    fun toViewState_contains_every_app_icon() {
        val screen = assertIs<ComponentsPreviewViewState>(PreviewScreenState.toViewState())

        assertEquals(AppIcon.entries.size, screen.items.size)
        assertEquals(AppIcon.entries.map { it.name }, screen.items.map { it.name })
    }
}
