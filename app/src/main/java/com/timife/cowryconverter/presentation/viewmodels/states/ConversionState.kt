package com.timife.cowryconverter.presentation.viewmodels.states

data class ConversionState(
    val fromAmount: String = "",
    val toAmount: String = "",
    val fromCurrency: String = "",
    val toCurrency: String = "",
    val result: String = "",
)