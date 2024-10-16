package com.logbook.myolwinoo.unitconverter.features.converter

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.logbook.myolwinoo.unitconverter.data.ConversionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

class ConverterViewModel: ViewModel() {

    var input1 = mutableStateOf(TextFieldValue(""))
        private set
    var unit1 = mutableStateOf(LengthUnit.Foot)
        private set

    var input2 = mutableStateOf(TextFieldValue(""))
        private set
    var unit2 = mutableStateOf(LengthUnit.Foot)
        private set

    private val _histories = MutableStateFlow<List<ConversionResult>>(emptyList())
    val histories: Flow<List<ConversionResult>> = _histories
        .map { it.sortedByDescending { it.timestamp } }

    val enableSaveBtn = snapshotFlow { input1.value.text.isNotBlank() && input2.value.text.isNotBlank() }

    fun onInput1Changed(newValue: TextFieldValue) {
        input1.value = newValue
        // update input2 when input1 has changed
        input2.value = calculate(newValue.text)
    }

    fun onUnit1Changed(newValue: LengthUnit) {
        unit1.value = newValue
        // update input2 when input1 unit has changed
        input2.value = calculate(input1.value.text)
    }

    fun onInput2Changed(newValue: TextFieldValue) {
        input2.value = newValue
        // update input1 when input2 has changed
        input1.value = calculate(newValue.text)
    }

    fun onUnit2Changed(newValue: LengthUnit) {
        unit2.value = newValue
        // update input1 when input2 unit has changed
        input1.value = calculate(input2.value.text)
    }

    fun clear() {
        input1.value = TextFieldValue("")
        input2.value = TextFieldValue("")
    }

    fun save() {
        _histories.update {
            it + ConversionResult(
                id = UUID.randomUUID().toString(),
                fromValue = input1.value.text,
                fromUnit = unit1.value.name,
                toValue = input2.value.text,
                toUnit = unit2.value.name,
                timestamp = System.currentTimeMillis()
            )
        }
    }

    private fun calculate(value: String): TextFieldValue {
        return LengthConverter.getFormattedLength(
            value = value,
            fromUnit = unit1.value,
            toUnit = unit2.value
        ).let { TextFieldValue(text = it, selection = TextRange(it.length)) }
    }
}