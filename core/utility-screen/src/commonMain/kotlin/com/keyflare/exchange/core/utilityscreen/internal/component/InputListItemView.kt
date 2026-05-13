package com.keyflare.exchange.core.utilityscreen.internal.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.icons.compose.list.Close
import com.keyflare.exchange.core.icons.compose.list.Copy
import com.keyflare.exchange.core.icons.compose.list.Paste
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState.ScreenNode.Leaf.ListItem

@Composable
internal fun InputListItemView(
    state: ListItem.Input,
    modifier: Modifier = Modifier,
) {
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp),
        ) {
            when (state) {
                is ListItem.Input.Text -> TextInput(state, modifier)
                is ListItem.Input.FloatNumber -> FloatNumberInput(state, modifier)
                is ListItem.Input.IntNumber -> IntNumberInput(state, modifier)
            }
        }
        if (state.drawLine) {
            Divider(
                color = CustomTheme.colors.divider,
                startIndent = 16.dp,
                modifier = Modifier
                    .height(0.5.dp)
                    .align(Alignment.BottomStart),
            )
        }
    }
}

@Composable
private fun TextInput(state: ListItem.Input.Text, modifier: Modifier = Modifier) {
    val value = remember(state.initialValue) {
        mutableStateOf(
            reduceTextInputSnapshot(
                previous = null,
                latestExternalValue = state.initialValue,
            )
        )
    }
    val clipboardManager = LocalClipboardManager.current

    InputLayout(
        value = value.value.displayedValue,
        label = state.label,
        modifier = modifier,
        onValueChange = {
            value.value = value.value.copy(displayedValue = it)
            state.onValueChange(it)
        },
        trailingContent = {
            if (state.allowCopy) {
                IconButton(
                    onClick = { clipboardManager.setText(AnnotatedString(value.value.displayedValue)) },
                    modifier = Modifier.offset(x = 32.dp),
                ) {
                    Icon(
                        imageVector = AppIcon.Compose.Copy,
                        tint = CustomTheme.colors.textAction,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
            IconButton(
                onClick = {
                    val text = clipboardManager.getText()?.text ?: ""
                    value.value = value.value.copy(displayedValue = text)
                    state.onValueChange(text)
                },
                modifier = Modifier.offset(x = 16.dp),
            ) {
                Icon(
                    imageVector = AppIcon.Compose.Paste,
                    tint = CustomTheme.colors.textAction,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                )
            }
            IconButton(
                onClick = {
                    val text = ""
                    value.value = value.value.copy(displayedValue = text)
                    state.onValueChange(text)
                },
            ) {
                Icon(
                    imageVector = AppIcon.Compose.Close,
                    tint = CustomTheme.colors.textAction,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                )
            }
        },
    )
}

internal data class TextInputSnapshot(
    val externalValue: String,
    val displayedValue: String,
)

internal fun reduceTextInputSnapshot(
    previous: TextInputSnapshot?,
    latestExternalValue: String,
): TextInputSnapshot {
    return if (previous == null || previous.externalValue != latestExternalValue) {
        TextInputSnapshot(
            externalValue = latestExternalValue,
            displayedValue = latestExternalValue,
        )
    } else {
        previous
    }
}

@Composable
private fun IntNumberInput(
    state: ListItem.Input.IntNumber,
    modifier: Modifier = Modifier,
) {
    InputLayout(
        value = state.value.toString(),
        label = state.label,
        onValueChange = { value ->
            state.onValueChange(value.filter { it.isDigit() }.toInt())
        },
        textAlign = TextAlign.End,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = modifier,
        trailingContent = {
            Spacer(modifier = Modifier.width(16.dp))
        },
    )
}

@Composable
private fun FloatNumberInput(
    state: ListItem.Input.FloatNumber,
    modifier: Modifier = Modifier,
) {
    InputLayout(
        value = state.value.toString(),
        label = state.label,
        onValueChange = { value ->
            val corrected = value
                .replace(",", ".")
                .filter { it.isDigit() || it == '.' }
            state.onValueChange(corrected.toFloat())
        },
        textAlign = TextAlign.End,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = modifier,
        trailingContent = {
            Spacer(modifier = Modifier.width(16.dp))
        },
    )
}

@Composable
private fun InputLayout(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: @Composable () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textAlign: TextAlign = TextAlign.Start,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(48.dp),
    ) {
        Text(
            text = label,
            style = CustomTheme.type.body3,
            color = CustomTheme.colors.textSecondary,
        )
        Spacer(modifier = Modifier.width(16.dp))
        BasicTextField(
            value = value,
            textStyle = CustomTheme.type.body2.copy(
                color = CustomTheme.colors.textPrimary,
                textAlign = textAlign,
            ),
            cursorBrush = SolidColor(CustomTheme.colors.textAction),
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.weight(1f),
        )
        trailingContent()
    }
}
