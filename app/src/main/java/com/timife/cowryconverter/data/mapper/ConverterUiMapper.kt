package com.timife.cowryconverter.data.mapper

import com.timife.cowryconverter.data.models.CurrenciesResponse
import com.timife.cowryconverter.data.models.RatesResponse
import com.timife.cowryconverter.domain.model.Currency
import com.timife.cowryconverter.domain.model.Rate

fun CurrenciesResponse.toCurrencies(): List<Currency> {
    return this.symbols?.map { (symbol, name) ->
        Currency(
            symbol = symbol,
            name = name
        )
    } ?: emptyList()
}

fun RatesResponse.toRates(): List<Rate> {
    return this.rates?.map { (currency, value)->
        Rate(
            currency = currency,
            rate = value
        )
    } ?: emptyList()
}


