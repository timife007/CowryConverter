package com.timife.cowryconverter.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timife.cowryconverter.domain.usecases.GetCurrenciesUC
import com.timife.cowryconverter.domain.usecases.GetRatesUC
import com.timife.cowryconverter.domain.utils.Resource
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

    fun getAllRates() {
        _ratesState.value = RatesState.Loading
        viewModelScope.launch {
            combine(
                ratesUC(),
                currenciesUC()
            ) { ratesResponse, currenciesResponse ->
                Pair(ratesResponse, currenciesResponse)
            }.collect { (ratesResponse, currenciesResponse) ->
                when {
                    ratesResponse is Resource.Success && currenciesResponse is Resource.Success -> {
                        val rates = ratesResponse.data?.map { rate ->
                            val currency =
                                currenciesResponse.data?.find { it.symbol == rate.currency }
                            RateUiModel(
                                symbol = rate.currency,
                                name = currency?.name ?: "",
                                rate = rate.rate
                            )
                        }

                        if (!rates.isNullOrEmpty()) {
                            _ratesState.value = RatesState.Success(rates)
                            _conversionState.update {
                                it.copy(
                                    fromCurrency = flowOf(rates.first()),
                                    toCurrency = flowOf(rates.first())
                                )
                            }
                        }
                    }

                    ratesResponse is Resource.Loading || currenciesResponse is Resource.Loading -> {
                        _ratesState.value = RatesState.Loading
                    }

                    ratesResponse is Resource.Error || currenciesResponse is Resource.Error -> {
                        _ratesState.value =
                            RatesState.Error("Failed to load rates/currencies. Check Internet connection and try again")
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
                )
            }
        }
    }

    fun convertCurrency(amount: String) {
        if (amount.isNotEmpty()) {
            _conversionState.update {
                it.copy(
                    toAmount = flow {
                        val exchangeRate = it.toCurrency.first().rate / it.fromCurrency.first().rate
                        emit((amount.toDouble() * exchangeRate).toString())
                    },
                )
            }
        }
    }
}