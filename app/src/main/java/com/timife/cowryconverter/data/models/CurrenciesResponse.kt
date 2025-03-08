package com.timife.cowryconverter.data.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CurrenciesResponse(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("symbols")
    val symbols: Map<String, String>?
)