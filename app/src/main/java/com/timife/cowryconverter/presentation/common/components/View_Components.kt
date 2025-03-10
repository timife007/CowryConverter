package com.timife.cowryconverter.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.timife.cowryconverter.R
import com.timife.cowryconverter.presentation.ui.theme.CowryConverterTheme
import com.timife.cowryconverter.presentation.ui.theme.Dimens
import com.timife.cowryconverter.presentation.viewmodels.data.RateUiModel

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
    topOption: @Composable BoxWithConstraintsScope.() -> Unit = {},
    view: @Composable BoxWithConstraintsScope.() -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = background)
    ) {
        topOption()
        view()
    }
}

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = stringResource(R.string.enter_an_amount),
    isSingleLine: Boolean = true,
    onTextChanged: (CharSequence) -> Unit = {},
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    trailingText: String = "",
    action: (txt: String) -> Unit = {},
    onClickTxtField: (String?) -> Unit = {},
) {
    val fieldValue = remember {
        mutableStateOf(text)
    }
    /**
     * the remember call only initializes its value once when the composable is first created.
     * when the text parameter changes, the fieldValue won't be updated automatically. So, the
     * the Launched effect helps with updating the fieldValue when the text parameter changes.
     */
    LaunchedEffect(key1 = text) {
        fieldValue.value = text
    }

    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = modifier.pointerInput(fieldValue) {
            awaitEachGesture {
                // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                // in the Initial pass to observe events before the text field consumes them
                // in the Main pass.
                awaitFirstDown(pass = PointerEventPass.Initial)
                val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                if (upEvent != null) {
                    onClickTxtField(null)
                }
            }
        },
        value = fieldValue.value,
        textStyle = MaterialTheme.typography.bodyLarge,
        onValueChange = {
            fieldValue.value = it
            onTextChanged(it)
        },
        singleLine = isSingleLine,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (fieldValue.value.isNotEmpty()) {
                    action(fieldValue.value)
                    focusManager.clearFocus()
                }
            }
        ),
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme
                    .typography
                    .bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        },
        shape = RoundedCornerShape(Dimens.grid_1),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        trailingIcon = {
            Text(
                text = trailingText,
                modifier = Modifier,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDropdownMenu(
    currencyOptions: List<RateUiModel>,
    selectedOption: RateUiModel,
    onSelectCurrency: (RateUiModel) -> Unit,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        modifier = modifier,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption.symbol,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.surface
            ),
            onValueChange = {

            },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.currencies_dropdown),
                    Modifier.clickable { expanded = !expanded },
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
                .border(
                    width = Dimens.grid_0_25,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(Dimens.grid_1)
                )

        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencyOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option.symbol,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    },
                    onClick = {
                        onSelectCurrency(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AppButton(
    text: String,
    loadingText: String = stringResource(R.string.converting),
    modifier: Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    buttonColor: Color = if (isEnabled) {
        MaterialTheme.colorScheme.secondary
    } else MaterialTheme.colorScheme.onSurface,
    contentColor: Color = MaterialTheme.colorScheme.onSecondary,
    action: () -> Unit,
) {

    val controller = LocalSoftwareKeyboardController.current
    Button(
        onClick = {
            controller?.hide()
            action()
        },
        enabled = if (isLoading) false else isEnabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = contentColor,
        ),
        shape = RoundedCornerShape(size = Dimens.grid_1)
    ) {
        Text(
            text = if (isLoading) loadingText else text.uppercase(),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                textAlign = TextAlign.Center,
                letterSpacing = 0.32.sp,
                fontWeight = FontWeight.SemiBold
            ),
        )
    }
}

@Composable
fun ClickableText(
    text: String,
    modifier: Modifier = Modifier,
    onClick:() -> Unit ={},
    color: Color
){
    TextButton(
        modifier = modifier,
        onClick = {
            onClick()
        }
    ) {
        Text(
            text = text,
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodySmall.copy(
                color = color,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
fun TextFieldPreview() {
    CowryConverterTheme {
        AppBackground {
            Column {
                AppTextField(
                    modifier = Modifier.fillMaxWidth(),
                    onTextChanged = {},
                    trailingText = "EUR",
                    hint = "Enter Amount"
                )

                OutlinedDropdownMenu(
                    modifier = Modifier,
                    onSelectCurrency = {},
                    selectedOption = RateUiModel(),
                    currencyOptions = listOf()
                )
            }
        }
    }
}