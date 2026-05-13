package com.keyflare.exchange.core.ds.component

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlin.test.Test
import kotlin.test.assertEquals

class DsValueTextFieldInputFilterTest {

    @Test
    fun keeps_digits() {
        val result = filterIncomingDecimalTextFieldValue(
            currentValue = TextFieldValue("12", selection = TextRange(2)),
            updatedValue = TextFieldValue("123", selection = TextRange(3)),
        )

        assertEquals("123", result.text)
        assertEquals(TextRange(3), result.selection)
    }

    @Test
    fun drops_letters_and_spaces_from_pasted_text() {
        val result = filterIncomingDecimalTextFieldValue(
            currentValue = TextFieldValue("", selection = TextRange(0)),
            updatedValue = TextFieldValue("12a 3x", selection = TextRange(6)),
        )

        assertEquals("123", result.text)
        assertEquals(TextRange(3), result.selection)
    }

    @Test
    fun keeps_first_decimal_separator_from_pasted_text() {
        val result = filterIncomingDecimalTextFieldValue(
            currentValue = TextFieldValue("", selection = TextRange(0)),
            updatedValue = TextFieldValue("12,3.4", selection = TextRange(6)),
        )

        assertEquals("12,34", result.text)
        assertEquals(TextRange(5), result.selection)
    }

    @Test
    fun ignores_second_decimal_separator_when_value_already_has_one() {
        val result = filterIncomingDecimalTextFieldValue(
            currentValue = TextFieldValue("1,2", selection = TextRange(1)),
            updatedValue = TextFieldValue("1.,2", selection = TextRange(2)),
        )

        assertEquals("1,2", result.text)
        assertEquals(TextRange(1), result.selection)
    }

    @Test
    fun allows_replacing_existing_decimal_separator_with_another_one() {
        val result = filterIncomingDecimalTextFieldValue(
            currentValue = TextFieldValue("1,2", selection = TextRange(1, 2)),
            updatedValue = TextFieldValue("1.2", selection = TextRange(2)),
        )

        assertEquals("1.2", result.text)
        assertEquals(TextRange(2), result.selection)
    }

    @Test
    fun preserves_selection_only_changes() {
        val result = filterIncomingDecimalTextFieldValue(
            currentValue = TextFieldValue("123", selection = TextRange(3)),
            updatedValue = TextFieldValue("123", selection = TextRange(1)),
        )

        assertEquals("123", result.text)
        assertEquals(TextRange(1), result.selection)
    }

    @Test
    fun preserves_selection_when_external_value_catches_up_after_local_change() {
        val result = syncTextFieldValueWithExternalValue(
            currentValue = TextFieldValue("12", selection = TextRange(2)),
            externalValue = "123",
            previousExternalValue = "12",
        )

        assertEquals("123", result.value.text)
        assertEquals(TextRange(2), result.value.selection)
        assertEquals("123", result.previousExternalValue)
    }

    @Test
    fun ignores_stale_external_value_after_local_text_change() {
        val result = syncTextFieldValueWithExternalValue(
            currentValue = TextFieldValue("123", selection = TextRange(3)),
            externalValue = "12",
            previousExternalValue = "12",
        )

        assertEquals("123", result.value.text)
        assertEquals(TextRange(3), result.value.selection)
        assertEquals("12", result.previousExternalValue)
    }
}
