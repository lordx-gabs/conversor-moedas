package com.example.conversormoedas.ui.mask

import android.icu.text.NumberFormat
import android.icu.util.Currency
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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
        val originalText = text.text
            .trim()

        val parts = originalText.split(".")

        val formatted = if (parts.size > 1) {
            parts[0] + parts[1].take(2)
        } else {
            originalText
                .filter { it.isDigit() }
        }

        if (formatted.isEmpty()) {
            val emptyFormatted = numberFormatter.format(0)
            return TransformedText(
                AnnotatedString(emptyFormatted),
                CurrencyOffsetMapping("", emptyFormatted)
            )
        }
        val parsed = BigDecimal(formatted).movePointLeft(2)

        val formattedText = numberFormatter.format(parsed)

        return TransformedText(
            AnnotatedString(formattedText),
            CurrencyOffsetMapping(originalText, formattedText)
        )
    }
}