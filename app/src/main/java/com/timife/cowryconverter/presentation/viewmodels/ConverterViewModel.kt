package com.timife.cowryconverter.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timife.cowryconverter.domain.usecases.GetCurrenciesUC
import com.timife.cowryconverter.domain.usecases.GetRatesUC
import com.timife.cowryconverter.domain.utils.Resource
import com.timife.cowryconverter.domain.utils.Resource.Success
import com.timife.cowryconverter.presentation.viewmodels.data.RateUiModel
import com.timife.cowryconverter.presentation.viewmodels.states.ConversionState
import com.timife.cowryconverter.presentation.viewmodels.states.RatesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val ratesUC: GetRatesUC,
    private val currenciesUC: GetCurrenciesUC
) : ViewModel() {

    private val _ratesState: MutableStateFlow<RatesState> = MutableStateFlow(RatesState.Loading)
    val ratesState: StateFlow<RatesState> = _ratesState.asStateFlow()

    private val _conversionState = MutableStateFlow(ConversionState())
    val conversionState: StateFlow<ConversionState> = _conversionState.asStateFlow()

    init {
        getAllRates()
    }


    //Combine responses from rates and comments to have unified responses.
    private fun getAllRates() {
        viewModelScope.launch {
            combine(
                ratesUC.invoke(),
                currenciesUC.invoke()
            ) { ratesResponse, currenciesResponse ->
                when {
                    ratesResponse is Success && currenciesResponse is Success -> {

                        val rates = ratesResponse.data?.map { rate ->
                            val currency =
                                currenciesResponse.data?.find { it.symbol == rate.currency }
                            RateUiModel(
                                symbol = rate.currency,
                                name = currency?.name ?: "",
                                rate = rate.rate
                            )
                        }
                        if (rates?.isNotEmpty() == true) {
                            _ratesState.value = RatesState.Success(rates)
                        }
                    }

                    ratesResponse is Resource.Loading || currenciesResponse is Resource.Loading -> {
                        _ratesState.value = RatesState.Loading
                    }
                }
            }
        }
    }

    fun selectFromCurrency(rate: RateUiModel) {
        _conversionState.update {
            it.copy(
                fromCurrency = flowOf(rate),
                fromAmount = flowOf(""),
                toAmount = flowOf("")
            )
        }
    }

    fun selectToCurrency(rate: RateUiModel) {
        _conversionState.update {
            it.copy(
                toCurrency = flowOf(rate),
                toAmount = flowOf(""),
                fromAmount = flowOf("")
            )
        }
    }

    //Ensure that toAmount is recalculated when fromAmount changes.
    fun updateFromAmount(amount: String) {
        if (amount.isNotEmpty()) {
            _conversionState.update {
                it.copy(
                    fromAmount = flowOf(amount),
                    toAmount = flow {
                        val exchangeRate = it.toCurrency.first().rate / it.fromCurrency.first().rate
                        emit((amount.toDouble() * exchangeRate).toString())
                    }
                )
            }
        }
    }

    //Ensure that fromAmount is recalculated when toAmount changes.
    fun updateToAmount(amount: String) {
        if (amount.isNotEmpty()) {
            _conversionState.update {
                it.copy(
                    toAmount = flowOf(amount),
                    fromAmount = flow {
                        val exchangeRate = it.fromCurrency.first().rate / it.toCurrency.first().rate
                        emit((amount.toDouble() * exchangeRate).toString())
                    }
                )
            }
        }
    }
}