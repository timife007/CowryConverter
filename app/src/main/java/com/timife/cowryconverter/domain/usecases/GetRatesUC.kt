package com.timife.cowryconverter.domain.usecases

import com.timife.cowryconverter.domain.model.Rate
import com.timife.cowryconverter.domain.repositories.ConverterRepo
import com.timife.cowryconverter.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRatesUC @Inject constructor(
    private val converterRepo: ConverterRepo
){
    operator fun invoke(): Flow<Resource<List<Rate>>> = converterRepo.getRates()
}