package com.logbook.myolwinoo.unitconverter.data

import android.text.format.DateUtils
import com.logbook.myolwinoo.unitconverter.features.converter.LengthUnit

data class ConversionResult(
    val id: String,

    val fromValue: String,
    val fromUnit: LengthUnit,

    val toValue: String,
    val toUnit: LengthUnit,

    val timestamp: Long
) {
    val timeAgo: String
        get() = DateUtils.getRelativeTimeSpanString(
            timestamp,
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS,

            ).toString()
}
