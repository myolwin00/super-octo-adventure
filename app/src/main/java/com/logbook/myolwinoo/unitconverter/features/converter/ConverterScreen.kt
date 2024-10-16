@file:OptIn(ExperimentalMaterial3Api::class)

package com.logbook.myolwinoo.unitconverter.features.converter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.logbook.myolwinoo.unitconverter.R
import kotlinx.coroutines.launch

@Composable
fun ConverterScreen(
    viewModel: ConverterViewModel = viewModel()
) {
    val enableSaveBtn = viewModel.enableSaveBtn.collectAsStateWithLifecycle(false)
    val histories = viewModel.histories.collectAsStateWithLifecycle(emptyList())

    val showHistorySheet = rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Length Converter")
                },
                actions = {
                    IconButton(onClick = {
                        showHistorySheet.value = true
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_history),
                            contentDescription = "History button"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        if (showHistorySheet.value) {
            HistorySheet(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                histories = histories.value,
                onDismiss = { showHistorySheet.value = false }
            )
        }

        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                    start = 20.dp,
                    end = 20.dp
                )
        ) {
            InputContainer(
                modifier = Modifier,
                value = viewModel.input1.value,
                onValueChange = viewModel::onInput1Changed,
                hint = "0.0",
                selectedUnit = viewModel.unit1.value,
                onUnitChange = viewModel::onUnit1Changed
            )
            Spacer(Modifier.size(20.dp))
            InputContainer(
                modifier = Modifier,
                value = viewModel.input2.value,
                onValueChange = viewModel::onInput2Changed,
                hint = "0.0",
                selectedUnit = viewModel.unit2.value,
                onUnitChange = viewModel::onUnit2Changed
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    modifier = Modifier,
                    onClick = viewModel::clear
                ) {
                    Text(text = "Clear")
                }
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    modifier = Modifier,
                    enabled = enableSaveBtn.value,
                    onClick = {
                        viewModel.save()
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("Saved!")
                        }
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}

@Composable
fun InputContainer(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    hint: String,
    selectedUnit: LengthUnit,
    onUnitChange: (LengthUnit) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = 12.dp,
                    bottom = 20.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LengthUnit.entries.forEach { unit ->
                    UnitSelector(
                        unit = unit,
                        selected = selectedUnit == unit,
                        onClick = { onUnitChange(unit) }
                    )
                }
            }
            Input(
                value = value,
                onValueChange = onValueChange,
                hint = hint,
                unit = selectedUnit
            )
        }
    }
}

@Composable
fun UnitSelector(
    modifier: Modifier = Modifier,
    unit: LengthUnit,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        modifier = modifier,
        onClick = onClick,
        label = {
            Text(
                unit.abbr,
                color = if (selected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
                },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = "Check icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        } else {
            null
        },
        shape = RoundedCornerShape(50),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun Input(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    hint: String,
    unit: LengthUnit
) {
    Row {
        Box(
            modifier = modifier
                .weight(1f)
                .alignByBaseline()
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.displayLarge
                    .copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End
                    ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
            )
            if (value.text.isEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = hint,
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.End
                )
            }
        }
//        Spacer(Modifier.size(4.dp))
//        Text(
//            modifier = Modifier.alignByBaseline(),
//            text = unit.abbr,
//            style = MaterialTheme.typography.displaySmall,
//            color = MaterialTheme.colorScheme.onSurface
//        )
    }
}

@Preview
@Composable
private fun ConverterScreenPreview() {
    ConverterScreen()
}