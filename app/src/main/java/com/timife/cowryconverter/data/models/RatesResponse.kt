package com.timife.cowryconverter.data.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RatesResponse(
    @SerializedName("success")
    val success:Boolean?,
    @SerializedName("base")
    val base: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("rates")
    val rates: Map<String, Double>?,
)