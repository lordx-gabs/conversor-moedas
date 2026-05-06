package com.example.conversormoedas.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyConversionResponse(
    val result: String,
    @SerialName("error_type")
    val errorType: String? = null,
    @SerialName("base_code")
    val baseCode: String,
    @SerialName("target_code")
    val targetCode: String,
    @SerialName("conversion_rate")
    val conversionRate: Double,
    @SerialName("conversion_result")
    val conversionResult: Double,
)
