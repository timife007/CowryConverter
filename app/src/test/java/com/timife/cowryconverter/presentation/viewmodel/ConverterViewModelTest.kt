package com.timife.cowryconverter.presentation.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.timife.cowryconverter.domain.model.Currency
import com.timife.cowryconverter.domain.model.Rate
import com.timife.cowryconverter.domain.usecases.GetCurrenciesUC
import com.timife.cowryconverter.domain.usecases.GetRatesUC
import com.timife.cowryconverter.domain.utils.Resource
import com.timife.cowryconverter.presentation.viewmodels.ConverterViewModel
import com.timife.cowryconverter.presentation.viewmodels.data.RateUiModel
import com.timife.cowryconverter.presentation.viewmodels.states.RatesState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ConverterViewModelTest {

    private val getRatesUC = mockk<GetRatesUC>()
    private val getCurrenciesUC = mockk<GetCurrenciesUC>()
    private lateinit var viewModel: ConverterViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun mockSuccessData(
        rates: List<Rate> = emptyList(),
        currencies: List<Currency> = emptyList()
    ) {
        coEvery { getRatesUC.invoke() } returns flowOf(Resource.Success(rates, "Fetch successful"))
        coEvery { getCurrenciesUC.invoke() } returns flowOf(Resource.Success(currencies, "Fetch successful"))
    }

    private fun mockErrorData(
        ratesError: Boolean = false,
        currenciesError: Boolean = false
    ) {
        if (ratesError) {
            coEvery { getRatesUC.invoke() } returns flowOf(Resource.Error("Rates failed"))
        } else {
            coEvery { getRatesUC.invoke() } returns flowOf(Resource.Success(emptyList(), "OK"))
        }

        if (currenciesError) {
            coEvery { getCurrenciesUC.invoke() } returns flowOf(Resource.Error("Currencies failed"))
        } else {
            coEvery { getCurrenciesUC.invoke() } returns flowOf(Resource.Success(emptyList(), "OK"))
        }
    }

    private fun launchViewModel() {
        viewModel = ConverterViewModel(getRatesUC, getCurrenciesUC)
    }

    @Test
    fun `getAllRates should emit success state when both use cases return success`() = runTest {
        val mockRates = listOf(Rate(currency = "USD", rate = 1.0))
        val mockCurrencies = listOf(Currency(symbol = "USD", name = "US Dollar"))
        mockSuccessData(rates = mockRates, currencies = mockCurrencies)
        launchViewModel()

        viewModel.ratesState.test {
            assertThat(awaitItem()).isInstanceOf(RatesState.Loading::class.java)
            advanceUntilIdle()
            val state = awaitItem()
            assertThat(state).isInstanceOf(RatesState.Success::class.java)
            val successState = state as RatesState.Success
            assertThat(successState.rates).hasSize(1)
            assertThat(successState.rates[0].symbol).isEqualTo("USD")
            assertThat(successState.rates[0].name).isEqualTo("US Dollar")
        }
    }

    @Test
    fun `getAllRates should emit error state if rates use case fails`() = runTest {
        mockErrorData(ratesError = true)
        launchViewModel()

        viewModel.ratesState.test {
            assertThat(awaitItem()).isInstanceOf(RatesState.Loading::class.java)
            advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(RatesState.Error::class.java)
        }
    }

    @Test
    fun `getAllRates should emit error state if currencies use case fails`() = runTest {
        mockErrorData(currenciesError = true)
        launchViewModel()

        viewModel.ratesState.test {
            assertThat(awaitItem()).isInstanceOf(RatesState.Loading::class.java)
            advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(RatesState.Error::class.java)
        }
    }

    @Test
    fun `selectFromCurrency should update fromCurrency in conversionState`() = runTest {
        mockSuccessData()
        launchViewModel()

        val rateUiModel = RateUiModel("GBP", "Pound", 1.5)
        viewModel.selectFromCurrency(rateUiModel)

        viewModel.conversionState.test {
            val state = awaitItem()
            val fromCurrency = state.fromCurrency.first()
            assertThat(fromCurrency.symbol).isEqualTo("GBP")
            assertThat(fromCurrency.name).isEqualTo("Pound")
            assertThat(fromCurrency.rate).isEqualTo(1.5)
        }
    }

    @Test
    fun `selectToCurrency should update toCurrency in conversionState`() = runTest {
        mockSuccessData()
        launchViewModel()

        val rateUiModel = RateUiModel("EUR", "Euro", 1.0)
        viewModel.selectToCurrency(rateUiModel)

        viewModel.conversionState.test {
            val state = awaitItem()
            val toCurrency = state.toCurrency.first()
            assertThat(toCurrency.symbol).isEqualTo("EUR")
            assertThat(toCurrency.name).isEqualTo("Euro")
            assertThat(toCurrency.rate).isEqualTo(1.0)
        }
    }

    @Test
    fun `updateFromAmount should update fromAmount in conversionState`() = runTest {
        mockSuccessData()
        launchViewModel()

        viewModel.updateFromAmount("500")

        viewModel.conversionState.test {
            val state = awaitItem()
            assertThat(state.fromAmount.first()).isEqualTo("500")
        }
    }

    @Test
    fun `convertCurrency should calculate correct toAmount`() = runTest {
        mockSuccessData()
        launchViewModel()

        val from = RateUiModel("USD", "Dollar", 1.0)
        val to = RateUiModel("NGN", "Naira", 1500.0)

        viewModel.selectFromCurrency(from)
        viewModel.selectToCurrency(to)
        viewModel.convertCurrency("2")

        viewModel.conversionState.test {
            val state = awaitItem()
            val result = state.toAmount.first()
            assertThat(result.toDouble()).isEqualTo(3000.0)
        }
    }
}

