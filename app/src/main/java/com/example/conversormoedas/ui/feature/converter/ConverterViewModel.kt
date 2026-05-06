package com.example.conversormoedas.ui.feature.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conversormoedas.domain.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConverterUiState())
    val uiState = _uiState.asStateFlow()
    private val _conversionState = MutableStateFlow<ConversionState>(ConversionState.Idle)
    val conversionState = _conversionState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                fromCurrenciesList = listOf("USD", "EUR", "BRL"),
                toCurrenciesList = listOf("BRL", "EUR", "USD"),
                fromCurrencySelected = "BRL",
                toCurrencySelected = "USD"
            )
        }
    }

    fun onUiEvent(event: ConverterUiEvent) {
        when (event) {
            is ConverterUiEvent.OnFromCurrencySelected -> {
                _uiState.update {
                    it.copy(fromCurrencySelected = event.currency)
                }
            }

            is ConverterUiEvent.OnFromCurrencyAmountChanged -> {
                _uiState.update {
                    it.copy(fromCurrencyAmount = event.amount)
                }
            }

            is ConverterUiEvent.OnToCurrencySelected -> {
                _uiState.update {
                    it.copy(toCurrencySelected = event.currency)
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
            val amount = _uiState.value.fromCurrencyAmount.toDoubleOrNull()

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