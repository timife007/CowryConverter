package com.timife.cowryconverter.data.repositoryImpls

import com.timife.cowryconverter.domain.utils.Resource
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseDataSource {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T?>): Resource<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Resource.Success(body, response.message())
                }
            } else {
                return Resource.Error(response.message())
            }
            return Resource.Error(response.message())
        } catch (e: HttpException) {
            return Resource.Error(e.message ?: "HTTP error")
        } catch (e: IOException) {
            return Resource.Error(e.message ?: "Network error") // No internet or timeout
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown error")
        }
    }
}