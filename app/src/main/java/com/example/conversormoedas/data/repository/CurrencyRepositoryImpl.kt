package com.example.conversormoedas.data.repository

import com.example.conversormoedas.data.network.KtorClient
import com.example.conversormoedas.domain.model.CurrencyConversion
import com.example.conversormoedas.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl  @Inject constructor(
    private val ktorClient: KtorClient
) : CurrencyRepository {
    override suspend fun convertCurrency(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Result<CurrencyConversion> {
        return runCatching {
            val response = ktorClient.convertCurrency(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
                amount = amount
            )

            CurrencyConversion(
                baseCode = response.baseCode,
                targetCode = response.targetCode,
                conversionRate = response.conversionRate.toString(),
                conversionResult = response.conversionResult.toString()
            )
        }
    }
}