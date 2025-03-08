package com.timife.cowryconverter.domain.repositories

import com.timife.cowryconverter.domain.model.Currency
import com.timife.cowryconverter.domain.model.Rate
import com.timife.cowryconverter.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ConverterRepo {

    fun getCurrencies(): Flow<Resource<List<Currency>>>

    fun getRates(): Flow<Resource<List<Rate>>>
}