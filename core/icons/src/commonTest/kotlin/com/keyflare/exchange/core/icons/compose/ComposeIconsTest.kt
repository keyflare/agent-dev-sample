package com.keyflare.exchange.core.icons.compose

import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.icons.compose.list.Divide
import com.keyflare.exchange.core.icons.compose.list.Equal
import com.keyflare.exchange.core.icons.compose.list.Minus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ComposeIconsTest {

    @Test
    fun every_app_icon_has_a_compose_vector() {
        AppIcon.entries.forEach { icon ->
            assertNotNull(icon.imageVector(), "Missing compose vector for $icon")
        }
    }

    @Test
    fun swap_icon_uses_its_own_name() {
        assertEquals(AppIcon.SWAP.name, requireNotNull(AppIcon.SWAP.imageVector()).name)
    }

    @Test
    fun converter_operation_icons_are_app_icons() {
        assertEquals(
            setOf(AppIcon.MINUS, AppIcon.DIVIDE, AppIcon.EQUAL),
            setOf(
                AppIcon.valueOf(AppIcon.Compose.Minus.name),
                AppIcon.valueOf(AppIcon.Compose.Divide.name),
                AppIcon.valueOf(AppIcon.Compose.Equal.name),
            ),
        )
    }
}
