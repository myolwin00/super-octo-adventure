package com.logbook.myolwinoo.unitconverter.data

import android.text.format.DateUtils

data class ConversionResult(
    val id: String,

    val fromValue: String,
    val fromUnit: String,

    val toValue: String,
    val toUnit: String,

    val timestamp: Long
) {
    val timeAgo: String
        get() = DateUtils.getRelativeTimeSpanString(
            timestamp,
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS,

            ).toString()
}
