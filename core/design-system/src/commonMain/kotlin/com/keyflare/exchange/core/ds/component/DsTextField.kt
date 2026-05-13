package com.keyflare.exchange.core.ds.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme

@Composable
fun DsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    contentAlpha: Float = 1f,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val colors = TextFieldDefaults.textFieldColors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        textColor = CustomTheme.colors.textPrimaryVariant.copy(alpha = contentAlpha),
        cursorColor = CustomTheme.colors.textAction.copy(alpha = contentAlpha),
        backgroundColor = CustomTheme.colors.surfaceSecondary,
    )

    val visualTransformation = VisualTransformation.None
    val singleLine = true
    val readOnly = false
    val isError = false
    val textStyle = CustomTheme.type.body1

    val mergedTextStyle = textStyle.merge(
        TextStyle(color = colors.textColor(enabled).value)
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
            value = value,
            modifier = modifier
                .clip(shape)
                .background(colors.backgroundColor(enabled).value)
                .defaultMinSize(
                    minWidth = TextFieldDefaults.MinWidth,
                    minHeight = TextFieldDefaults.MinHeight
                ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(colors.cursorColor(isError).value),
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = value,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = {
                        Text(
                            text = placeholder,
                            style = CustomTheme.type.body1,
                            color = CustomTheme.colors.textAdditional.copy(alpha = contentAlpha),
                        )
                    },
                    singleLine = singleLine,
                    enabled = enabled,
                    interactionSource = remember { MutableInteractionSource() },
                    colors = colors,
                    contentPadding = PaddingValues(horizontal = 12.dp)
                )
            }
        )
    }
}
