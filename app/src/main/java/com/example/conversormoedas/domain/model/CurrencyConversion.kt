package com.example.conversormoedas.domain.model

data class CurrencyConversion(
    val baseCode: String,
    val targetCode: String,
    val conversionRate: String,
    val conversionResult: String
)
