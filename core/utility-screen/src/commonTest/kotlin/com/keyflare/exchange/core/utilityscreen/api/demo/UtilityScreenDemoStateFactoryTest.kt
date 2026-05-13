package com.keyflare.exchange.core.utilityscreen.api.demo

import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UtilityScreenDemoStateFactoryTest {

    @Test
    fun createGeneralScreen_returnsExpectedReusableStructure() {
        val screen = UtilityScreenDemoStateFactory.createGeneralScreen()

        assertEquals("utility-demo", screen.id)
        assertEquals("Utility Screen Demo", screen.title)
        assertEquals(2, screen.nodes.size)

        val actionsGroup = assertIs<UtilityScreenViewState.ScreenNode.Group>(screen.nodes.first())
        assertEquals("Actions", actionsGroup.title)

        val firstItem =
            assertIs<UtilityScreenViewState.ScreenNode.Leaf.ListItem.Simple>(actionsGroup.nodes.first())
        assertEquals(
            UtilityScreenUiEvent.OnNodeClick(nodeId = "details"),
            firstItem.onClick,
        )
    }
}
