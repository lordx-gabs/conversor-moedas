package com.example.conversormoedas.domain.repository

import com.example.conversormoedas.domain.model.CurrencyConversion

interface CurrencyRepository {
    suspend fun convertCurrency(fromCurrency: String, toCurrency: String, amount: Double): Result<CurrencyConversion>
}