package com.keyflare.common.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class DisplayNumberFormatterTest {

    @Test
    fun display_number_rounds_and_compacts_large_numbers_by_thousand_orders() {
        assertEquals("12 346K", formatDisplayNumber("12345678"))
        assertEquals("12 346K", formatDisplayNumber("12345678.12345"))
        assertEquals("123.5KKK", formatDisplayNumber("123456789123"))
        assertEquals("123KKKK", formatDisplayNumber("123456789123456"))
        assertEquals("1KKKKKK", formatDisplayNumber("1234567891234567890"))
    }

    @Test
    fun display_number_keeps_full_integer_when_it_fits_result_limit() {
        assertEquals("1 234 567", formatDisplayNumber("1234567"))
        assertEquals("1 234 567", formatDisplayNumber("1234567.89"))
    }

    @Test
    fun display_number_uses_up_to_five_significant_digits_for_regular_numbers() {
        assertEquals("1 234.1", formatDisplayNumber("1234.1234567890"))
        assertEquals("12.123", formatDisplayNumber("12.1234567890"))
        assertEquals("1.1235", formatDisplayNumber("1.1234567890"))
        assertEquals("0.12346", formatDisplayNumber("0.1234567890"))
        assertEquals("0.012346", formatDisplayNumber("0.0123456789"))
        assertEquals("0.001235", formatDisplayNumber("0.0012345678"))
        assertEquals("123", formatDisplayNumber("123.00001234"))
        assertEquals("1.1", formatDisplayNumber("1.1000032"))
    }

    @Test
    fun display_number_formats_tiny_numbers_with_scientific_notation() {
        assertEquals("1.235e-6", formatDisplayNumber("0.0000012345"))
        assertEquals("1.2e-6", formatDisplayNumber("0.0000012"))
        assertEquals("1.235e-9", formatDisplayNumber("0.00000000123456789"))
    }

    @Test
    fun display_number_counts_suffixes_and_scientific_markers_toward_result_limit() {
        assertEquals("123KKKK", formatDisplayNumber("123456789123456"))
        assertEquals("1.23e-10", formatDisplayNumber("0.00000000012345"))
    }

    @Test
    fun display_number_preserves_sign_and_omits_plus() {
        assertEquals("-12.346", formatDisplayNumber("-12.345678"))
        assertEquals("12.346", formatDisplayNumber("+12.345678"))
    }

    @Test
    fun display_number_returns_zero_for_zero_values() {
        assertEquals("0", formatDisplayNumber("0"))
        assertEquals("0", formatDisplayNumber("000.0000"))
    }

    @Test
    fun display_number_returns_invalid_text_unchanged() {
        assertEquals("12a34", formatDisplayNumber("12a34"))
        assertEquals("1.2.3", formatDisplayNumber("1.2.3"))
    }

    @Test
    fun display_rate_uses_display_number_rules() {
        assertEquals("1.2346", formatDisplayRate(1.23456789))
        assertEquals("76 262", formatDisplayRate(76_262.0))
        assertEquals("1 600 000", formatDisplayRate(1_600_000.0))
        assertEquals("1.235e-6", formatDisplayRate(0.0000012345))
    }
}
