package com.timife.cowryconverter.presentation.common.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import com.timife.cowryconverter.R
import kotlinx.serialization.json.Json.Default.configuration

@Composable
fun V_MultiStyleText(
    text: String,
    disableSecondText: Boolean = false,
    color1: Color,
    color2: Color,
    modifier: Modifier,
    textDecoration: TextDecoration? = null,
    style: TextStyle,
    textAlign: TextAlign = TextAlign.Start,
) {
    var s = text.split("\n")
    if (s.size < 2) s = text.split(" ")
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = color1,
                )
            ) {
                append(s[0])
            }
            withStyle(style = SpanStyle(color = Color.Transparent)) {
                append("\n")
            }
            withStyle(
                style = SpanStyle(
                    color = color2,
                )
            ) {
                append(if (disableSecondText) "" else s[1])
            }
        },
        modifier = modifier,
        textDecoration = textDecoration,
        style = style.copy(
            textAlign = textAlign,
            lineHeight = TextUnit.Unspecified
        )
    )
}

@Composable
fun AppBackground(
    background: Color = MaterialTheme.colorScheme.background,
    topOption: @Composable (modifier: Modifier, onClick: () -> Unit) -> Unit = { _, _ -> },
    view: @Composable BoxWithConstraintsScope.() -> Unit,
) {
    val controller = LocalSoftwareKeyboardController.current
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = background)
    ) {
        topOption(
            Modifier.align(Alignment.TopEnd), {
                controller?.hide()
            }
        )
        view()
    }
}