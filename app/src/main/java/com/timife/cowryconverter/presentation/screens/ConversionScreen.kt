package com.timife.cowryconverter.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.timife.cowryconverter.R
import com.timife.cowryconverter.presentation.common.components.AppBackground
import com.timife.cowryconverter.presentation.common.components.AppButton
import com.timife.cowryconverter.presentation.common.components.AppTextField
import com.timife.cowryconverter.presentation.common.components.ClickableText
import com.timife.cowryconverter.presentation.common.components.OutlinedDropdownMenu
import com.timife.cowryconverter.presentation.common.components.V_MultiStyleText
import com.timife.cowryconverter.presentation.ui.theme.CowryConverterTheme
import com.timife.cowryconverter.presentation.ui.theme.Dimens
import com.timife.cowryconverter.presentation.viewmodels.ConverterViewModel
import com.timife.cowryconverter.presentation.viewmodels.states.RatesState


@Composable
fun ConversionScreen(
    modifier: Modifier = Modifier,
    viewModel: ConverterViewModel = hiltViewModel()
) {
    val rateState =
        viewModel.ratesState.collectAsState()
    val conversionState = viewModel.conversionState.collectAsState()

    val fromAmount = conversionState.value.fromAmount.collectAsState(initial = "")
    val toAmount = conversionState.value.toAmount.collectAsState(initial = "")

    AppBackground {
        when(val state = rateState.value){
            is RatesState.Error -> {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(Dimens.grid_1_5),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.error, style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.error
                        ), textAlign = TextAlign.Center, modifier = Modifier
                    )
                    AppButton(text = stringResource(R.string.retry), modifier = Modifier) {
                        viewModel.getAllRates()
                    }
                }
            }
            RatesState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(Dimens.grid_5),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            is RatesState.Success -> {
                val fromCurrency = conversionState.value.fromCurrency.collectAsState(
                    initial = state.rates.first()
                )
                val toCurrency = conversionState.value.toCurrency.collectAsState(
                    initial = state.rates.first()
                )
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(vertical = Dimens.grid_5),
                    verticalArrangement = Arrangement.spacedBy(Dimens.grid_2),
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Dimens.grid_2),
                            modifier = Modifier.padding(horizontal = Dimens.grid_2)
                        ) {
                            V_MultiStyleText(
                                text = stringResource(R.string.currency_calculator),
                                color1 = MaterialTheme.colorScheme.primaryContainer,
                                color2 = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )

                            AppTextField(
                                modifier = Modifier
                                    .padding(
                                        top = Dimens.grid_5,
                                    )
                                    .fillMaxWidth(),
                                text = fromAmount.value,
                                onTextChanged = {
                                    viewModel.updateFromAmount(it.toString())
                                },
                                trailingText = fromCurrency.value.symbol
                            )
                            AppTextField(
                                modifier = Modifier
                                    .padding(
                                        top = Dimens.grid_2
                                    )
                                    .fillMaxWidth(),
                                hint = stringResource(R.string.conversion),
                                text = toAmount.value,
                                isEnabled = false,
                                trailingText = toCurrency.value.symbol
                            )

                            Row(
                                modifier = Modifier.padding(top = Dimens.grid_4),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Dimens.grid_2)
                            ) {
                                OutlinedDropdownMenu(
                                    modifier = Modifier.weight(1f),
                                    currencyOptions = state.rates,
                                    selectedOption = fromCurrency.value,
                                    onSelectCurrency = {
                                        viewModel.selectFromCurrency(it)
                                    }
                                )
                                IconButton(
                                    onClick = {},
                                    modifier = Modifier.size(Dimens.grid_4)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_conversion_arrow),
                                        contentDescription = stringResource(R.string.conversion_arrow),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                OutlinedDropdownMenu(
                                    modifier = Modifier.weight(1f),
                                    currencyOptions = state.rates,
                                    selectedOption = toCurrency.value,
                                    onSelectCurrency = {
                                        viewModel.selectToCurrency(it)
                                    }
                                )
                            }

                            AppButton(
                                modifier = Modifier
                                    .padding(top = Dimens.grid_3)
                                    .fillMaxWidth(),
                                text = stringResource(R.string.convert)
                            ) {
                                viewModel.convertCurrency(fromAmount.value)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ClickableText(
                                    text = stringResource(R.string.mid_market_exchange_rate_at_13_38_utc),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                )
                                IconButton(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                        .size(Dimens.grid_5),
                                    enabled = true,
                                    colors = IconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.tertiary,
                                        contentColor = MaterialTheme.colorScheme.primaryContainer,
                                        disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                                        disabledContentColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    onClick = {}
                                ) {
                                    Text(
                                        text = "i",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        GraphSheet(
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }

        }
    }
}


@Composable
@Preview
fun ConversionScreenPreview() {
    CowryConverterTheme {
        ConversionScreen()
    }
}