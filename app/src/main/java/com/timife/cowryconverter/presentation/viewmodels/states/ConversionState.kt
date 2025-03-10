package com.timife.cowryconverter.presentation.viewmodels.states

import com.timife.cowryconverter.presentation.viewmodels.data.RateUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


/**
 * A data class that holds the state of the conversion screen.
 * @param fromAmount The amount to convert from.
 * @param toAmount The amount to convert to.
 * @param fromCurrency The currency to convert from.
 * @param toCurrency The currency to convert to.
 */
data class ConversionState(
    val fromAmount: Flow<String> = flowOf(""),
    val toAmount: Flow<String> = flowOf(""),
    val fromCurrency: Flow<RateUiModel> = flowOf(RateUiModel()),
    val toCurrency: Flow<RateUiModel> = flowOf(RateUiModel()),
)