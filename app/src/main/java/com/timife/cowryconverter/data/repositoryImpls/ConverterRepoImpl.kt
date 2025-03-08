package com.timife.cowryconverter.data.repositoryImpls

import com.timife.cowryconverter.data.mapper.toCurrencies
import com.timife.cowryconverter.data.mapper.toRates
import com.timife.cowryconverter.data.network.ConverterApiService
import com.timife.cowryconverter.domain.model.Currency
import com.timife.cowryconverter.domain.model.Rate
import com.timife.cowryconverter.domain.repositories.ConverterRepo
import com.timife.cowryconverter.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class ConverterRepoImpl @Inject constructor(
    private val converterApiService: ConverterApiService,
) : ConverterRepo, BaseDataSource() {
    override fun getCurrencies(): Flow<Resource<List<Currency>>> {
        return flow {
            when (val response = safeApiCall { converterApiService.getCurrencies() }) {
                is Resource.Success -> {
                    emit(Resource.Success(response.data?.toCurrencies(), response.message))
                }

                is Resource.Error -> {
                    emit(Resource.Error(response.message))
                }

                is Resource.Loading -> {
                    emit(Resource.Loading())
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getRates(): Flow<Resource<List<Rate>>> {
        return flow {
            when (val response = safeApiCall { converterApiService.getRates() }) {
                is Resource.Success -> {
                    emit(Resource.Success(response.data?.toRates(), response.message))
                }

                is Resource.Error -> {
                    emit(Resource.Error(response.message))
                }

                is Resource.Loading -> {
                    emit(Resource.Loading())
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}