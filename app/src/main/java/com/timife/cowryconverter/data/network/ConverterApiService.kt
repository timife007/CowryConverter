package com.timife.cowryconverter.data.network

import com.timife.cowryconverter.data.models.CurrenciesResponse
import com.timife.cowryconverter.data.models.RatesResponse
import retrofit2.Response
import retrofit2.http.GET

interface ConverterApiService {

    //fetch all available currencies and symbols
    @GET("symbols")
    suspend fun getCurrencies(): Response<CurrenciesResponse?>

    //fetch all individual rates with the default base currency as EUR
    @GET("latest")
    suspend fun getRates(): Response<RatesResponse?>

}