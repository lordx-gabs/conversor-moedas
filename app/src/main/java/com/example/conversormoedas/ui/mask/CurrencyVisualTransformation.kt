package com.example.conversormoedas.ui.mask

import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.text.isDigitsOnly
import java.math.BigDecimal

class CurrencyVisualTransformation(
    currencyCode: String
) : VisualTransformation {

    private val numberFormatter = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance(currencyCode)
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text.trim()
        if (originalText.isEmpty()) {
            val emptyFormatted = numberFormatter.format(0)
            return TransformedText(
                AnnotatedString(emptyFormatted),
                CurrencyOffsetMapping("", emptyFormatted)
            )
        }
        if (originalText.isDigitsOnly().not()) {
            Log.w("TAG", "Prize visual transformation require using digits only but found [$originalText]")
            return TransformedText(text, OffsetMapping.Identity)
        }

        val parsed = BigDecimal(originalText).movePointLeft(2)
        val formattedText = numberFormatter.format(parsed)

        return TransformedText(
            AnnotatedString(formattedText),
            CurrencyOffsetMapping(originalText, formattedText)
        )
    }
}