package com.example.conversormoedas.ui.feature.converter

data class ConverterUiState(
    val fromCurrenciesList: List<String> = emptyList(),
    val toCurrenciesList: List<String> = emptyList(),
    val fromCurrencySelected: String = "",
    val toCurrencySelected: String = "",
    val fromCurrencyAmount: String = "",
    val toCurrencyAmount: String = "",
)
