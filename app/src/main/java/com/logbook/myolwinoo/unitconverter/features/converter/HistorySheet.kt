@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.logbook.myolwinoo.unitconverter.features.converter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.logbook.myolwinoo.unitconverter.data.ConversionResult

@Composable
fun HistorySheet(
    modifier: Modifier = Modifier,
    histories: List<ConversionResult>,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                top = 0.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            )
        ) {
            stickyHeader {
                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    text = "Conversion History",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(
                items = histories,
                key = { it.id }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 20.dp)
                ) {
                    Text(text = it.fromValue, style = MaterialTheme.typography.displayLarge)
                    Text(text = it.fromUnit)
                    Text(text = it.toValue)
                    Text(text = it.toUnit)
                    Text(text = it.timeAgo)
                }
            }
        }
    }
}