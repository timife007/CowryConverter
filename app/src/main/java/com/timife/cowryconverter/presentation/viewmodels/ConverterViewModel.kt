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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val ratesUC: GetRatesUC,
    private val currenciesUC: GetCurrenciesUC
): ViewModel() {

    private val _ratesState = MutableStateFlow(RatesState())
    val ratesState : StateFlow<RatesState> = _ratesState.asStateFlow()

    private val _conversionState = MutableStateFlow(ConversionState())
    val conversionState : StateFlow<ConversionState> = _conversionState.asStateFlow()

    init {
        getAllRates()
    }


    //Combine responses from rates and comments to have unified responses.
    private fun getAllRates(){
        viewModelScope.launch {
            combine(ratesUC.invoke(), currenciesUC.invoke()){ ratesResponse, currenciesResponse ->
                when {
                    ratesResponse is Success && currenciesResponse is Success -> {

                        val rates = ratesResponse.data?.map { rate ->
                            val currency = currenciesResponse.data?.find { it.symbol == rate.currency }
                            RateUiModel(
                                symbol = rate.currency,
                                name = currency?.name ?: "",
                                rate = rate.rate
                            )
                        }
                        if(rates?.isNotEmpty() == true){
                            _ratesState.value = ratesState.value.copy(
                                rates = rates
                            )
                        }
                    }

                    ratesResponse is Resource.Loading || currenciesResponse is Resource.Loading -> {
                        _ratesState.value = ratesState.value.copy(
                            loading = true
                        )
                    }
                }
            }
        }
    }
}