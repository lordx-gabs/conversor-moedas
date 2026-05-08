package com.example.conversormoedas.ui.feature.converter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.conversormoedas.R
import com.example.conversormoedas.ui.components.CurrencyField
import com.example.conversormoedas.ui.theme.ConversorMoedasTheme

@Composable
fun ConverterScreen() {
    val viewModel = viewModel<ConverterViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val conversionState by viewModel.conversionState.collectAsStateWithLifecycle()

    ConverterContent(
        uiState,
        onUiEvent = viewModel::onUiEvent,
        conversionState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterContent(
    uiState: ConverterUiState,
    onUiEvent: (ConverterUiEvent) -> Unit,
    conversionState: ConverterViewModel.ConversionState
) {
    Scaffold(
        modifier = Modifier
            .imePadding()
            .fillMaxHeight(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Conversor de Moedas",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding)
                .padding(8.dp)
                .consumeWindowInsets(innerPadding)
                .systemBarsPadding()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Column {
                        CurrencyField(
                            currencies = uiState.fromCurrenciesList,
                            selectedCurrency = uiState.fromCurrencySelected,
                            currencyAmount = uiState.fromCurrencyAmount,
                            onCurrencySelected = {
                                onUiEvent(ConverterUiEvent.OnFromCurrencySelected(it))
                            },
                            onCurrencyAmountChanged = {
                                onUiEvent(ConverterUiEvent.OnFromCurrencyAmountChanged(it))
                            }
                        )

                        CurrencyField(
                            currencies = uiState.toCurrenciesList,
                            selectedCurrency = uiState.toCurrencySelected,
                            currencyAmount = uiState.toCurrencyAmount,
                            onCurrencySelected = {
                                onUiEvent(ConverterUiEvent.OnToCurrencySelected(it))
                            },
                            onCurrencyAmountChanged = {},
                            isEnabled = false,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            when (conversionState) {
                                ConverterViewModel.ConversionState.Idle -> {
                                    // no ui
                                }

                                ConverterViewModel.ConversionState.Loading -> {
                                    CircularProgressIndicator()
                                }

                                ConverterViewModel.ConversionState.Success -> {
                                    Text(
                                        text = "Conversão realizada com sucesso!",
                                        color = Color.Green
                                    )
                                }

                                is ConverterViewModel.ConversionState.Error -> {
                                    Text(
                                        text = conversionState.errorMessage,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_downward),
                            contentDescription = null,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }

                Button(
                    onClick = {
                        onUiEvent(ConverterUiEvent.SendConverterForm)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd),
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Text(text = "Converter Moeda")
                }
            }
        }
    }
}

@Preview
@Composable
private fun ConverterContentPreview() {
    ConversorMoedasTheme {
        ConverterContent(
            uiState = ConverterUiState(
                fromCurrenciesList = listOf("USD", "EUR", "BRL"),
                toCurrenciesList = listOf("BRL", "EUR", "USD"),
                fromCurrencySelected = "BRL",
                toCurrencySelected = "USD"
            ),
            onUiEvent = {},
            conversionState = ConverterViewModel.ConversionState.Idle
        )
    }
}