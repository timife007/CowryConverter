package com.timife.cowryconverter.domain.usecases

import com.timife.cowryconverter.domain.model.Currency
import com.timife.cowryconverter.domain.repositories.ConverterRepo
import com.timife.cowryconverter.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrenciesUC @Inject constructor(
    private val converterRepo: ConverterRepo
){
    operator fun invoke(): Flow<Resource<List<Currency>>> = converterRepo.getCurrencies()
}