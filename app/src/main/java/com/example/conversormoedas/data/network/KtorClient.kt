package com.example.conversormoedas.data.network

import com.example.conversormoedas.data.network.model.CurrencyConversionResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class KtorClient @Inject constructor(private val client: HttpClient) {
    suspend fun convertCurrency(
        fromCurrency: String,
        toCurrency: String,
        amount: Double

    ): CurrencyConversionResponse {
        return client.get("pair/$fromCurrency/$toCurrency/$amount").body()
    }
}