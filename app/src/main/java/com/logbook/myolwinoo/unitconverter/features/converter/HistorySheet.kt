@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.logbook.myolwinoo.unitconverter.features.converter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.logbook.myolwinoo.unitconverter.R
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
            ),
        ) {
            stickyHeader {
                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    text = "Conversion History",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            itemsIndexed(
                items = histories,
                key = { _, item -> item.id }
            ) { i, item ->
                if (i != 0) {
                    HorizontalDivider()
                }
                HistoryItem(item = item)
            }
        }
    }
}

@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    item: ConversionResult
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .alignByBaseline()
                .padding(end = 10.dp),
        ) {
            Text(
                text = item.fromValue,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alignByBaseline()
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = item.fromUnit.full,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.alignByBaseline()
            )
            Icon(
                painter = painterResource(R.drawable.ic_equal),
                contentDescription = "Equal Icon",
                modifier = Modifier
                    .alignByBaseline()
                    .padding(horizontal = 10.dp)
            )
            Text(
                text = item.toValue,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alignByBaseline()
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = item.toUnit.full,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.alignByBaseline()
            )
        }
        Text(
            text = item.timeAgo,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier
                .alignByBaseline()
        )
    }
}

@Preview
@Composable
private fun HistoryItemPreview() {
    HistoryItem(
        item = ConversionResult(
            id = "1",
            fromValue = "1",
            fromUnit = LengthUnit.Foot,
            toValue = "3",
            toUnit = LengthUnit.Meter,
            timestamp = System.currentTimeMillis()
        )
    )
}