package com.logbook.myolwinoo.unitconverter.features.converter

import java.text.DecimalFormat

object LengthConverter {

    fun getFormattedLength(value: String, fromUnit: LengthUnit, toUnit: LengthUnit): String {
        val doubleValue = value.toDoubleOrNull() ?: return ""
        return try {
            val convertedValue = convert(doubleValue, fromUnit, toUnit)
            format(convertedValue)
        } catch (t: Throwable) {
            ""
        }
    }

    private fun convert(value: Double, fromUnit: LengthUnit, toUnit: LengthUnit): Double {
        // Define conversion factors (using meters as the base unit)
        val conversionFactors = mapOf(
            LengthUnit.Meter to 1.0,
            LengthUnit.Millimeter to 1000.0,
            LengthUnit.Mile to 0.000621371,
            LengthUnit.Foot to 3.28084
        )

        // Get conversion factors for fromUnit and toUnit
        val fromFactor = conversionFactors[fromUnit] ?: throw IllegalArgumentException("Invalid 'fromUnit' value: $fromUnit")
        val toFactor = conversionFactors[toUnit] ?: throw IllegalArgumentException("Invalid 'toUnit' value: $toUnit")

        // Perform the conversion
        return value * (toFactor / fromFactor)
    }

    private fun format(value: Double): String {
        val decimalFormat = DecimalFormat("#.####")
        return decimalFormat.format(value)
    }
}