package com.timife.cowryconverter.presentation.viewmodels.states

import com.timife.cowryconverter.presentation.viewmodels.data.RateUiModel


sealed class RatesState {
    /**
     * A data class that holds the success state of the rates screen.
     * @param rates The list of rates to display.
     */
    data class Success(val rates: List<RateUiModel>) : RatesState()
    data class Error(val error: String) : RatesState()
    data object Loading : RatesState()
}