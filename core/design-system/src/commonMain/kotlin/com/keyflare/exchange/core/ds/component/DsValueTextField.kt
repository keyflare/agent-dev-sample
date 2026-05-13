package com.keyflare.exchange.core.ds.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.ds.common.addIf
import com.keyflare.exchange.core.ds.theme.ExchangeTheme

@Composable
fun DsValueTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    suffix: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onFocusChanged: ((Boolean) -> Unit)? = null,
) {
    var textFieldValueState by remember {
        mutableStateOf(value.asTextFieldValueWithSelectionAtEnd())
    }
    var previousExternalValue by remember { mutableStateOf(value) }
    var isFocused by remember { mutableStateOf(false) }
    var forceSelectionToEndOnNextSelectionChange by remember { mutableStateOf(false) }
    val externalSyncResult = syncTextFieldValueWithExternalValue(
        currentValue = textFieldValueState,
        externalValue = value,
        previousExternalValue = previousExternalValue,
    )
    val textFieldValue = externalSyncResult.value
    if (textFieldValueState != externalSyncResult.value) {
        textFieldValueState = externalSyncResult.value
    }
    if (previousExternalValue != externalSyncResult.previousExternalValue) {
        previousExternalValue = externalSyncResult.previousExternalValue
    }

    val colors = textFieldColors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        textColor = CustomTheme.colors.textSecondary,
        cursorColor = CustomTheme.colors.textAction,
        backgroundColor = if (!enabled) {
            CustomTheme.colors.surfaceSecondary
        } else {
            CustomTheme.colors.background
        },
    )

    val singleLine = true
    val readOnly = false
    val textStyle = CustomTheme.type.body1

    val mergedTextStyle = textStyle.merge(
        TextStyle(
            color = colors.textColor(enabled).value,
            textAlign = TextAlign.End,
        )
    )

    val customTextSelectionColors = TextSelectionColors(
        handleColor = CustomTheme.colors.textAction,
        backgroundColor = CustomTheme.colors.textAction.copy(alpha = 0.4f),
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors
    ) {
        @OptIn(ExperimentalMaterialApi::class)
        BasicTextField(
            value = textFieldValue,
            modifier = modifier
                .onFocusChanged { focusState ->
                    if (!isFocused && focusState.isFocused) {
                        forceSelectionToEndOnNextSelectionChange = true
                    }
                    textFieldValueState = textFieldValueState.moveSelectionToEndOnFocusLoss(
                        wasFocused = isFocused,
                        isFocused = focusState.isFocused,
                    )
                    if (isFocused && !focusState.isFocused) {
                        forceSelectionToEndOnNextSelectionChange = false
                    }
                    isFocused = focusState.isFocused
                    onFocusChanged?.invoke(focusState.isFocused)
                }
                .defaultMinSize(
                    minWidth = TextFieldDefaults.MinWidth,
                    minHeight = 30.dp
                ),
            onValueChange = { updatedValue ->
                val changeResult = handleIncomingTextFieldValueChange(
                    currentValue = textFieldValueState,
                    updatedValue = updatedValue,
                    isFocused = isFocused,
                    forceSelectionToEndOnNextSelectionChange = forceSelectionToEndOnNextSelectionChange,
                )
                textFieldValueState = changeResult.value
                forceSelectionToEndOnNextSelectionChange =
                    changeResult.forceSelectionToEndOnNextSelectionChange
                if (changeResult.value.text != value) {
                    onValueChange(changeResult.value.text)
                }
            },
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(colors.cursorColor(isError).value),
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            decorationBox = @Composable { innerTextField ->
                DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    label = label,
                    placeholder = placeholder,
                    suffix = suffix,
                    isError = isError,
                )
            }
        )
    }
}

internal fun String.asTextFieldValueWithSelectionAtEnd(): TextFieldValue {
    return TextFieldValue(
        text = this,
        selection = TextRange(length),
    )
}

internal fun TextFieldValue.withSelectionAtEnd(): TextFieldValue {
    return copy(selection = TextRange(text.length))
}

internal data class DsValueTextFieldExternalSyncResult(
    val value: TextFieldValue,
    val previousExternalValue: String,
)

internal fun syncTextFieldValueWithExternalValue(
    currentValue: TextFieldValue,
    externalValue: String,
    previousExternalValue: String,
): DsValueTextFieldExternalSyncResult {
    if (externalValue == previousExternalValue) {
        return DsValueTextFieldExternalSyncResult(
            value = currentValue,
            previousExternalValue = previousExternalValue,
        )
    }

    val value = currentValue.copy(
        text = externalValue,
        selection = TextRange(
            start = currentValue.selection.start.coerceAtMost(externalValue.length),
            end = currentValue.selection.end.coerceAtMost(externalValue.length),
        ),
    )
    return DsValueTextFieldExternalSyncResult(
        value = value,
        previousExternalValue = externalValue,
    )
}

internal fun TextFieldValue.moveSelectionToEndOnFocusLoss(
    wasFocused: Boolean,
    isFocused: Boolean,
): TextFieldValue {
    return if (wasFocused && !isFocused) {
        withSelectionAtEnd()
    } else {
        this
    }
}

internal fun TextFieldValue.normalizeSelectionChange(isFocused: Boolean): TextFieldValue {
    return if (isFocused) {
        this
    } else {
        withSelectionAtEnd()
    }
}

internal data class DsValueTextFieldChangeResult(
    val value: TextFieldValue,
    val forceSelectionToEndOnNextSelectionChange: Boolean,
)

internal fun filterIncomingDecimalTextFieldValue(
    currentValue: TextFieldValue,
    updatedValue: TextFieldValue,
): TextFieldValue {
    if (updatedValue.text == currentValue.text) {
        return updatedValue
    }

    val change = inferTextReplacementChange(
        currentText = currentValue.text,
        updatedText = updatedValue.text,
    )
    val filteredReplacement = filterDecimalReplacement(
        currentText = currentValue.text,
        replacementStart = change.currentStart,
        replacementEnd = change.currentEnd,
        replacement = change.replacement,
    )
    val filteredText = buildString {
        append(currentValue.text.substring(0, change.currentStart))
        append(filteredReplacement)
        append(currentValue.text.substring(change.currentEnd))
    }
    val caretOffset = change.currentStart + filteredReplacement.length

    return updatedValue.copy(
        text = filteredText,
        selection = TextRange(caretOffset),
    )
}

internal fun handleIncomingTextFieldValueChange(
    currentValue: TextFieldValue,
    updatedValue: TextFieldValue,
    isFocused: Boolean,
    forceSelectionToEndOnNextSelectionChange: Boolean,
): DsValueTextFieldChangeResult {
    if (updatedValue.text != currentValue.text) {
        val filteredValue = filterIncomingDecimalTextFieldValue(
            currentValue = currentValue,
            updatedValue = updatedValue,
        )
        return DsValueTextFieldChangeResult(
            value = filteredValue,
            forceSelectionToEndOnNextSelectionChange = false,
        )
    }

    val shouldForceSelectionToEnd =
        !isFocused || forceSelectionToEndOnNextSelectionChange

    return DsValueTextFieldChangeResult(
        value = if (shouldForceSelectionToEnd) {
            updatedValue.withSelectionAtEnd()
        } else {
            updatedValue
        },
        forceSelectionToEndOnNextSelectionChange = false,
    )
}

private data class TextReplacementChange(
    val currentStart: Int,
    val currentEnd: Int,
    val replacement: String,
)

private fun inferTextReplacementChange(
    currentText: String,
    updatedText: String,
): TextReplacementChange {
    var prefixLength = 0
    val maxPrefixLength = minOf(currentText.length, updatedText.length)
    while (
        prefixLength < maxPrefixLength &&
        currentText[prefixLength] == updatedText[prefixLength]
    ) {
        prefixLength++
    }

    var currentSuffixStart = currentText.length
    var updatedSuffixStart = updatedText.length
    while (
        currentSuffixStart > prefixLength &&
        updatedSuffixStart > prefixLength &&
        currentText[currentSuffixStart - 1] == updatedText[updatedSuffixStart - 1]
    ) {
        currentSuffixStart--
        updatedSuffixStart--
    }

    return TextReplacementChange(
        currentStart = prefixLength,
        currentEnd = currentSuffixStart,
        replacement = updatedText.substring(prefixLength, updatedSuffixStart),
    )
}

private fun filterDecimalReplacement(
    currentText: String,
    replacementStart: Int,
    replacementEnd: Int,
    replacement: String,
): String {
    val textWithoutReplacement = currentText.removeRange(replacementStart, replacementEnd)
    var hasDecimalSeparator = textWithoutReplacement.any(Char::isDecimalSeparator)

    return buildString {
        replacement.forEach { char ->
            when {
                char.isDigit() -> append(char)
                char.isDecimalSeparator() && !hasDecimalSeparator -> {
                    append(char)
                    hasDecimalSeparator = true
                }
            }
        }
    }
}

private fun Char.isDecimalSeparator(): Boolean {
    return this == '.' || this == ','
}

@Composable
private fun DecorationBox(
    value: String,
    innerTextField: @Composable () -> Unit,
    enabled: Boolean,
    label: String? = null,
    placeholder: String? = null,
    suffix: String? = null,
    isError: Boolean = false,
) {
    val shape = RoundedCornerShape(12.dp)
    val accentColor = if (isError) {
        CustomTheme.colors.textAlert
    } else {
        CustomTheme.colors.textAdditional
    }
    val borderColor = if (isError) {
        CustomTheme.colors.textAlert
    } else {
        CustomTheme.colors.surfaceSecondary
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clip(shape)
            .addIf(enabled) {
                border(
                    width = 1.dp,
                    color = borderColor,
                    shape = shape,
                )
            }
            .addIf(!enabled) {
                background(color = CustomTheme.colors.surfaceSecondary)
            }
            .padding(end = 12.dp)
            .padding(vertical = 12.dp)
    ) {
        if (label != null) {
            Text(
                text = label,
                color = accentColor,
                style = CustomTheme.type.body2,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(1f),
        ) {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.weight(1f),
            ) {
                if (value.isBlank() && !placeholder.isNullOrBlank()) {
                    Text(
                        text = placeholder,
                        color = CustomTheme.colors.textAdditional,
                        style = CustomTheme.type.body1,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                innerTextField()
            }

            if (!suffix.isNullOrBlank()) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = suffix,
                    color = CustomTheme.colors.textSecondary,
                    style = CustomTheme.type.body1,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldPreview() {
    var text by remember { mutableStateOf("test text") }

    @Composable
    fun Layout() {
        Column(
            Modifier
                .background(CustomTheme.colors.background)
                .padding(30.dp)
        ) {
            DsValueTextField(
                value = text,
                onValueChange = { text = it },
                label = "Label:",
            )
            Spacer(Modifier.height(12.dp))
            DsValueTextField(
                value = text,
                onValueChange = { text = it },
                label = "Label:",
                enabled = false
            )
            Spacer(Modifier.height(12.dp))
            DsValueTextField(
                value = "",
                onValueChange = { text = it },
                label = "Label:",
                placeholder = "Enter value"
            )
            Spacer(Modifier.height(12.dp))
            DsValueTextField(
                value = "",
                onValueChange = { text = it },
                label = "Label:",
                enabled = false,
                placeholder = "Enter value"
            )
        }
    }

    Column(
        modifier = Modifier
            .background(CustomTheme.colors.background)
    ) {
        ExchangeTheme(darkTheme = false) {
            Layout()
        }
        ExchangeTheme(darkTheme = true) {
            Layout()
        }
    }
}
