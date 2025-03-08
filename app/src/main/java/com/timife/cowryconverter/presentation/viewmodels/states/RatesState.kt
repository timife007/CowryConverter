package com.timife.cowryconverter.presentation.viewmodels.states

import com.timife.cowryconverter.presentation.viewmodels.data.RateUiModel

data class RatesState(
    val rates: List<RateUiModel> = emptyList(),
    val error: String = "",
    val loading: Boolean? = false,
)