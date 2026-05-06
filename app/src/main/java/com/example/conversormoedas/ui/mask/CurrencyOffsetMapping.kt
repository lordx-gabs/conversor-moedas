package com.example.conversormoedas.ui.mask

import androidx.compose.ui.text.input.OffsetMapping

class CurrencyOffsetMapping(originalText: String, formattedText: String) : OffsetMapping {
    private val originalLength: Int = originalText.length
    private val formattedLength: Int = formattedText.length
    private val indexes = findDigitIndexes(originalText, formattedText)

    private fun findDigitIndexes(firstString: String, secondString: String): List<Int> {
        val digitIndexes = mutableListOf<Int>()
        var currentIndex = 0
        for (digit in firstString) {
            val index = secondString.indexOf(digit, currentIndex)
            if (index != -1) {
                digitIndexes.add(index)
                currentIndex = index + 1
            }
        }
        return digitIndexes
    }

    override fun originalToTransformed(offset: Int): Int {
        // Se a lista de índices estiver vazia (texto original vazio), 
        // coloca o cursor no final do texto formatado (ex: R$ 0,00|)
        if (indexes.isEmpty()) return formattedLength
        
        // Se o offset for maior que o texto original, coloca no final do último dígito
        if (offset >= originalLength) {
            return indexes.last() + 1
        }
        
        // Mapeia a posição do caractere original para sua posição no texto formatado
        return indexes.getOrElse(offset) { formattedLength }
    }

    override fun transformedToOriginal(offset: Int): Int {
        if (indexes.isEmpty()) return 0
        
        // Se o cursor no formatado estiver depois do último dígito, mapeia para o final do original
        if (offset > indexes.last()) return originalLength
        
        // Encontra qual caractere original corresponde à posição atual no formatado
        val index = indexes.indexOfFirst { it >= offset }
        return if (index == -1) originalLength else index
    }
}
