package com.example.conversormoedas.ui.feature.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conversormoedas.domain.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConverterUiState())
    val uiState = _uiState.asStateFlow()
    private val _conversionState = MutableStateFlow<ConversionState>(ConversionState.Idle)
    val conversionState = _conversionState.asStateFlow()
    private val _allCurrencies = listOf("BRL", "EUR", "USD")

    init {
        _uiState.update { it ->
            it.copy(
                fromCurrenciesList = _allCurrencies,
                toCurrenciesList = _allCurrencies.filter {
                    it != "BRL"
                },
                fromCurrencySelected = "BRL",
                toCurrencySelected = "USD"
            )
        }
    }

    fun onUiEvent(event: ConverterUiEvent) {
        when (event) {
            is ConverterUiEvent.OnFromCurrencySelected -> {
                _uiState.update {
                    val toCurrencyList = _allCurrencies
                        .filter { currency -> currency != event.currency }
                        .sorted()

                    it.copy(
                        fromCurrencySelected = event.currency,
                        toCurrenciesList = toCurrencyList,
                        toCurrencySelected = if (event.currency == it.toCurrencySelected) toCurrencyList[0] else it.toCurrencySelected,
                        toCurrencyAmount = if (event.currency == it.toCurrencySelected) "0" else it.toCurrencyAmount,
                    )
                }
            }

            is ConverterUiEvent.OnFromCurrencyAmountChanged -> {
                _uiState.update {
                    it.copy(fromCurrencyAmount = event.amount)
                }
            }

            is ConverterUiEvent.OnToCurrencySelected -> {
                _uiState.update {
                    it.copy(
                        toCurrencySelected = event.currency,
                        toCurrencyAmount = if (event.currency != it.fromCurrencySelected && it.toCurrencyAmount != "0") "0" else it.toCurrencyAmount,
                    )
                }
            }

            ConverterUiEvent.SendConverterForm -> {
                convertCurrency()
            }
        }
    }

    private fun convertCurrency() {
        viewModelScope.launch {
            val fromCurrency = _uiState.value.fromCurrencySelected
            val toCurrency = _uiState.value.toCurrencySelected
            val amount = _uiState.value.fromCurrencyAmount
                .toBigDecimalOrNull()
                ?.divide(BigDecimal(100))
                ?.toDouble()

            if (fromCurrency.isNotBlank() && toCurrency.isNotBlank() && amount != null) {
                _conversionState.update {
                    ConversionState.Loading
                }

                currencyRepository.convertCurrency(
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    amount = amount
                ).fold(
                    onSuccess = { currencyConversion ->
                        _uiState.update {
                            it.copy(
                                toCurrencyAmount = currencyConversion.conversionResult
                            )
                        }

                        updateConversionStateSuccess()
                    },
                    onFailure = { error ->
                        updateConversionStateError(error.message ?: "Erro desconhecido.")
                    }
                )
            } else {
                updateConversionStateError("Valores inválidos.")
            }
        }
    }

    private fun updateConversionStateError(errorMessage: String) {
        _conversionState.update {
            ConversionState.Error(errorMessage)
        }
    }

    private fun updateConversionStateSuccess() {
        _conversionState.update {
            ConversionState.Success
        }
    }

    sealed interface ConversionState {
        object Idle : ConversionState
        object Loading : ConversionState
        object Success : ConversionState
        data class Error(val errorMessage: String) : ConversionState
    }
}