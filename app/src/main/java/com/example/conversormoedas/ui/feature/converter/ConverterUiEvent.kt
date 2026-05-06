package com.example.conversormoedas.ui.feature.converter

sealed interface ConverterUiEvent {
    data class OnFromCurrencySelected(val currency: String) : ConverterUiEvent
    data class OnToCurrencySelected(val currency: String) : ConverterUiEvent
    data class OnFromCurrencyAmountChanged(val amount: String) : ConverterUiEvent
    data object SendConverterForm : ConverterUiEvent
}