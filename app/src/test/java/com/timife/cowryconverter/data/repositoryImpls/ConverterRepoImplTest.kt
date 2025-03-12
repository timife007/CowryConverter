package com.timife.cowryconverter.data.repositoryImpls

import com.google.common.truth.Truth.assertThat
import com.timife.cowryconverter.data.models.CurrenciesResponse
import com.timife.cowryconverter.data.models.RatesResponse
import com.timife.cowryconverter.data.network.ConverterApiService
import com.timife.cowryconverter.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class ConverterRepoImplTest {

    private val apiService = mockk<ConverterApiService>()
    private lateinit var repo: ConverterRepoImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = ConverterRepoImpl(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCurrencies should emit success when API call is successful`() = runTest {
        val mockResponse = CurrenciesResponse(
            success = true,
            symbols = mapOf("USD" to "United States Dollar", "NGN" to "Naira")
        )
        val apiResult = Response.success(mockResponse)
        coEvery { apiService.getCurrencies() } returns apiResult

        // Act
        val result = repo.getCurrencies().first()

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data).hasSize(2)
        assertThat(data?.get(0)?.symbol).isEqualTo("USD")
        assertThat(data?.get(0)?.name).isEqualTo("United States Dollar")
    }

    @Test
    fun `getCurrencies should emit error on failed API response`() = runTest {
        val errorBody = "Error".toResponseBody("application/json".toMediaTypeOrNull())
        val response = Response.error<CurrenciesResponse>(400, errorBody)
        coEvery { apiService.getCurrencies() } returns response

        val result = repo.getCurrencies().first()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Response.error()")
    }

    @Test
    fun `getCurrencies should emit error on IOException`() = runTest {
        coEvery { apiService.getCurrencies() } throws IOException("No internet")

        val result = repo.getCurrencies().first()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("No internet")
    }

    @Test
    fun `getRates should emit success when API call is successful`() = runTest {
        val mockResponse = RatesResponse(
            success = true,
            base = "USD",
            date = "2024-01-01",
            rates = mapOf("USD" to 1.0, "NGN" to 1300.0)
        )
        val apiResult = Response.success(mockResponse)
        coEvery { apiService.getRates() } returns apiResult

        val result = repo.getRates().first()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data).hasSize(2)
        assertThat(data?.get(0)?.currency).isEqualTo("USD")
        assertThat(data?.get(0)?.rate).isEqualTo(1.0)
    }

    @Test
    fun `getRates should emit error on failed API response`() = runTest {
        val errorBody = "Rate error".toResponseBody("application/json".toMediaTypeOrNull())
        val response = Response.error<RatesResponse>(400, errorBody)
        coEvery { apiService.getRates() } returns response

        val result = repo.getRates().first()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Response.error()")
    }

    @Test
    fun `getRates should emit error on IOException`() = runTest {
        coEvery { apiService.getRates() } throws IOException("Timeout")

        val result = repo.getRates().first()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Timeout")
    }
}
