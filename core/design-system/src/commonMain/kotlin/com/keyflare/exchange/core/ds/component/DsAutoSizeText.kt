package com.keyflare.exchange.core.ds.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.keyflare.exchange.core.ds.common.DonutHole

@Immutable
data class DsAutoSizeTextState(
    val text: AnnotatedString,
    val fontSize: TextUnit,
)

@Composable
fun DsAutoSizeText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    val state = remember {
        mutableStateOf(DsAutoSizeTextState(AnnotatedString(""), style.fontSize))
    }

    Box(contentAlignment = Alignment.Center) {
        DonutHole {
            Text(
                modifier = modifier,
                text = state.value.text,
                color = color,
                textDecoration = textDecoration,
                textAlign = textAlign,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                style = style,
                fontSize = state.value.fontSize,
                softWrap = false,
            )
        }
        Text(
            modifier = modifier.drawWithContent {},
            text = text,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = TextOverflow.Clip,
            maxLines = 1,
            style = style,
            softWrap = false,
            onTextLayout = { result ->
                val multiplier = result.size.width.toFloat() / result.multiParagraph.width
                state.value = DsAutoSizeTextState(text, style.fontSize * multiplier)
            }
        )
    }
}

@Composable
fun DsAutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    DsAutoSizeText(
        modifier = modifier,
        text = AnnotatedString(text),
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        style = style,
    )
}
