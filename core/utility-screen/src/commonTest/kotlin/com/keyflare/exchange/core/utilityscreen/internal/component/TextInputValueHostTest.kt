package com.keyflare.exchange.core.utilityscreen.internal.component

import kotlin.test.Test
import kotlin.test.assertEquals

class TextInputValueHostTest {

    @Test
    fun `external value change resets displayed text to latest value`() {
        val previous = TextInputSnapshot(
            externalValue = "http://old-stage:8080",
            displayedValue = "http://manually-edited:8080",
        )

        val snapshot = reduceTextInputSnapshot(
            previous = previous,
            latestExternalValue = "http://new-stage:8080",
        )

        assertEquals("http://new-stage:8080", snapshot.displayedValue)
        assertEquals("http://new-stage:8080", snapshot.externalValue)
    }

    @Test
    fun `same external value preserves in-progress edits`() {
        val previous = TextInputSnapshot(
            externalValue = "http://stage:8080",
            displayedValue = "http://stage:9090",
        )

        val snapshot = reduceTextInputSnapshot(
            previous = previous,
            latestExternalValue = "http://stage:8080",
        )

        assertEquals(previous, snapshot)
    }
}
