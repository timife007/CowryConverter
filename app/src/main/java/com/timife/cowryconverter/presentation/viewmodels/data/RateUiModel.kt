package com.timife.cowryconverter.presentation.viewmodels.data

/**
 * A data class that holds the state of each currency and it's rate
 * @param symbol The symbol of the currency.
 * @param name The name of the currency.
 * @param rate The rate of the currency with respect to the base currency(EUR)
 */
data class RateUiModel(
    val symbol: String = "",
    val name: String = "",
    val rate: Double = 0.0
)