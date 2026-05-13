package com.keyflare.common.utils

import kotlin.math.max

fun formatDisplayRate(rate: Double): String {
    if (!rate.isFinite()) {
        return rate.toString()
    }

    return formatDisplayNumber(rate.toString())
}

fun formatDisplayNumber(
    number: String,
    indents: Boolean = true,
    trimDecimals: Boolean = true,
): String {
    val decimal = parseDisplayDecimal(number) ?: return number
    if (decimal.isZero) return "0"

    val significant = decimal.significant
    val sign = if (decimal.isNegative) "-" else ""

    val formattedMagnitude = when {
        significant.exponent < DisplayNumberFormatRules.ScientificThresholdExponent ->
            formatScientific(significant, signLength = sign.length)

        decimal.integerSignificantLength > DisplayNumberFormatRules.MaxResultChars ->
            formatCompact(significant, signLength = sign.length, indents = indents)

        else ->
            formatFixed(decimal, signLength = sign.length, indents = indents, trimDecimals = trimDecimals)
    }

    return sign + formattedMagnitude
}

private object DisplayNumberFormatRules {
    const val MaxSignificantDigits = 5
    const val MaxResultChars = 7
    const val OrderGroupSize = 3
    const val ScientificThresholdExponent = -5
    const val CompactSuffixChar = 'K'
}

private data class DisplayDecimal(
    val isNegative: Boolean,
    val integerDigits: String,
    val fractionDigits: String,
) {
    val isZero: Boolean = (integerDigits + fractionDigits).all { it == '0' }

    val integerSignificantLength: Int =
        integerDigits.trimStart('0').length

    val significant: SignificantDecimal by lazy {
        val allDigits = integerDigits + fractionDigits
        val firstSignificantIndex = allDigits.indexOfFirst { it != '0' }
        SignificantDecimal(
            digits = allDigits.substring(firstSignificantIndex),
            exponent = integerDigits.length - firstSignificantIndex - 1,
        )
    }
}

private data class SignificantDecimal(
    val digits: String,
    val exponent: Int,
)

private data class RoundedSignificantDecimal(
    val digits: String,
    val exponent: Int,
)

private data class RoundedFixedDecimal(
    val integerDigits: String,
    val fractionDigits: String,
)

private fun parseDisplayDecimal(rawNumber: String): DisplayDecimal? {
    val trimmed = rawNumber.trim()
    if (trimmed.isEmpty()) return null

    val withoutSpaces = trimmed.replace(" ", "")
    val normalized = withoutSpaces.replace(",", ".")
    val isNegative = normalized.startsWith("-")
    val unsigned = normalized.removePrefix("-").removePrefix("+")
    if (unsigned.isEmpty()) return null

    val expanded = expandScientificNotation(unsigned) ?: unsigned
    if (expanded.count { it == '.' } > 1) return null

    val integerPart = expanded.substringBefore('.', missingDelimiterValue = expanded)
        .ifEmpty { "0" }
    val fractionPart = expanded.substringAfter('.', missingDelimiterValue = "")
    if ((integerPart + fractionPart).isEmpty()) return null
    if ((integerPart + fractionPart).any { !it.isDigit() }) return null

    return DisplayDecimal(
        isNegative = isNegative,
        integerDigits = integerPart,
        fractionDigits = fractionPart,
    )
}

private fun expandScientificNotation(unsignedNumber: String): String? {
    val markerIndex = unsignedNumber.indexOfFirst { it == 'e' || it == 'E' }
    if (markerIndex < 0) return null

    val mantissa = unsignedNumber.substring(0, markerIndex)
    val exponent = unsignedNumber.substring(markerIndex + 1).toIntOrNull() ?: return null
    if (mantissa.isEmpty()) return null
    if (mantissa.count { it == '.' } > 1) return null

    val integerPart = mantissa.substringBefore('.', missingDelimiterValue = mantissa)
    val fractionPart = mantissa.substringAfter('.', missingDelimiterValue = "")
    val mantissaDigits = integerPart + fractionPart
    if (mantissaDigits.isEmpty() || mantissaDigits.any { !it.isDigit() }) return null

    val decimalIndex = integerPart.length + exponent
    return when {
        decimalIndex <= 0 ->
            "0." + "0".repeat(-decimalIndex) + mantissaDigits

        decimalIndex >= mantissaDigits.length ->
            mantissaDigits + "0".repeat(decimalIndex - mantissaDigits.length)

        else ->
            mantissaDigits.substring(0, decimalIndex) + "." + mantissaDigits.substring(decimalIndex)
    }
}

private fun formatScientific(
    significant: SignificantDecimal,
    signLength: Int,
): String {
    val exponentText = significant.exponent.toString()
    val exponentMarkerLength = 1 + exponentText.length
    val digitBudget = (DisplayNumberFormatRules.MaxResultChars - signLength - exponentMarkerLength)
        .coerceAtLeast(1)
        .coerceAtMost(DisplayNumberFormatRules.MaxSignificantDigits)
    val rounded = roundSignificant(significant, digitBudget)

    val mantissa = buildString {
        append(rounded.digits.first())
        val fraction = rounded.digits.drop(1).trimEnd('0')
        if (fraction.isNotEmpty()) {
            append('.')
            append(fraction)
        }
    }

    return mantissa + "e" + rounded.exponent
}

private fun formatCompact(
    significant: SignificantDecimal,
    signLength: Int,
    indents: Boolean,
): String {
    val maxSuffixLength = DisplayNumberFormatRules.MaxResultChars - signLength - 1
    for (suffixLength in 1..maxSuffixLength) {
        val digitBudget = minOf(
            DisplayNumberFormatRules.MaxSignificantDigits,
            DisplayNumberFormatRules.MaxResultChars - signLength - suffixLength,
        )
        if (digitBudget <= 0) continue

        val shifted = significant.copy(
            exponent = significant.exponent - suffixLength * DisplayNumberFormatRules.OrderGroupSize,
        )
        val formatted = formatSignificantDecimal(
            significant = shifted,
            digitBudget = digitBudget,
            indents = indents,
        ) ?: continue
        val result = formatted + DisplayNumberFormatRules.CompactSuffixChar.toString().repeat(suffixLength)
        if (result.countedLength <= DisplayNumberFormatRules.MaxResultChars - signLength) {
            return result
        }
    }

    val fallbackSuffixLength = maxSuffixLength.coerceAtLeast(1)
    val fallback = roundSignificant(
        significant = significant.copy(
            exponent = significant.exponent - fallbackSuffixLength * DisplayNumberFormatRules.OrderGroupSize,
        ),
        digitBudget = 1,
    )
    return fallback.digits.first().toString() +
        DisplayNumberFormatRules.CompactSuffixChar.toString().repeat(fallbackSuffixLength)
}

private fun formatFixed(
    decimal: DisplayDecimal,
    signLength: Int,
    indents: Boolean,
    trimDecimals: Boolean,
): String {
    val integer = decimal.integerDigits.trimStart('0').ifEmpty { "0" }
    val integerIsSignificant = decimal.integerSignificantLength > 0
    val decimalsToKeep = if (integerIsSignificant) {
        val remainingResultChars = DisplayNumberFormatRules.MaxResultChars - signLength - integer.length
        val remainingSignificantDigits = if (trimDecimals) {
            DisplayNumberFormatRules.MaxSignificantDigits - decimal.integerSignificantLength
        } else {
            remainingResultChars
        }
        minOf(remainingResultChars, remainingSignificantDigits).coerceAtLeast(0)
    } else {
        val leadingFractionZeros = decimal.fractionDigits.takeWhile { it == '0' }.length
        val remainingResultChars = DisplayNumberFormatRules.MaxResultChars - signLength - 1
        val targetDecimals = if (trimDecimals) {
            leadingFractionZeros + DisplayNumberFormatRules.MaxSignificantDigits
        } else {
            decimal.fractionDigits.length
        }
        minOf(remainingResultChars, targetDecimals).coerceAtLeast(0)
    }

    if (decimalsToKeep == 0) {
        return groupInteger(integer, indents)
    }

    val rounded = roundToFractionDigits(decimal, decimalsToKeep)
    val roundedInteger = rounded.integerDigits.trimStart('0').ifEmpty { "0" }
    val roundedFraction = rounded.fractionDigits.trimEnd('0')

    return if (roundedFraction.isEmpty()) {
        groupInteger(roundedInteger, indents)
    } else {
        groupInteger(roundedInteger, indents) + "." + roundedFraction
    }
}

private fun formatSignificantDecimal(
    significant: SignificantDecimal,
    digitBudget: Int,
    indents: Boolean,
): String? {
    val rounded = roundSignificant(significant, digitBudget)
    val integerDigitCount = rounded.exponent + 1

    val integer: String
    val fraction: String
    when {
        integerDigitCount >= rounded.digits.length -> {
            integer = rounded.digits + "0".repeat(integerDigitCount - rounded.digits.length)
            fraction = ""
        }

        integerDigitCount > 0 -> {
            integer = rounded.digits.take(integerDigitCount)
            fraction = rounded.digits.drop(integerDigitCount).trimEnd('0')
        }

        else -> {
            integer = "0"
            fraction = "0".repeat(-integerDigitCount) + rounded.digits.trimEnd('0')
        }
    }

    val result = if (fraction.isEmpty()) {
        groupInteger(integer, indents)
    } else {
        groupInteger(integer, indents) + "." + fraction
    }

    return result.takeIf { it.countedLength <= digitBudget }
}

private fun roundSignificant(
    significant: SignificantDecimal,
    digitBudget: Int,
): RoundedSignificantDecimal {
    val safeBudget = max(1, digitBudget)
    val paddedDigits = significant.digits.padEnd(safeBudget + 1, '0')
    val keptDigits = paddedDigits.take(safeBudget)
    val nextDigit = paddedDigits[safeBudget]
    val roundedDigits = if (nextDigit >= '5') {
        incrementDigits(keptDigits)
    } else {
        keptDigits
    }

    return if (roundedDigits.length > safeBudget) {
        RoundedSignificantDecimal(
            digits = roundedDigits.take(safeBudget),
            exponent = significant.exponent + 1,
        )
    } else {
        RoundedSignificantDecimal(
            digits = roundedDigits,
            exponent = significant.exponent,
        )
    }
}

private fun roundToFractionDigits(
    decimal: DisplayDecimal,
    decimals: Int,
): RoundedFixedDecimal {
    val integer = decimal.integerDigits.ifEmpty { "0" }
    val keepCount = integer.length + decimals
    val allDigits = (integer + decimal.fractionDigits).padEnd(keepCount + 1, '0')
    val keptDigits = allDigits.take(keepCount)
    val nextDigit = allDigits[keepCount]
    val rounded = if (nextDigit >= '5') {
        incrementDigits(keptDigits)
    } else {
        keptDigits
    }

    val integerLength = rounded.length - decimals
    return RoundedFixedDecimal(
        integerDigits = rounded.take(integerLength),
        fractionDigits = rounded.drop(integerLength).padEnd(decimals, '0'),
    )
}

private fun incrementDigits(digits: String): String {
    val chars = digits.toCharArray()
    for (index in chars.indices.reversed()) {
        if (chars[index] != '9') {
            chars[index] = chars[index] + 1
            return chars.concatToString()
        }
        chars[index] = '0'
    }

    return "1" + chars.concatToString()
}

private fun groupInteger(
    integer: String,
    indents: Boolean,
): String {
    if (!indents) return integer

    return integer.reversed()
        .chunked(DisplayNumberFormatRules.OrderGroupSize)
        .joinToString(" ")
        .reversed()
}

private val String.countedLength: Int
    get() = count { it != '.' && it != ' ' }
