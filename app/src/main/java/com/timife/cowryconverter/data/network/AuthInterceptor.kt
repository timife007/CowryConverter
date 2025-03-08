package com.timife.cowryconverter.data.network

import com.timife.cowryconverter.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        //Append the access_key to every request
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("access_key", BuildConfig.API_KEY)
            .build()

        //Build the new request with the new url
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}