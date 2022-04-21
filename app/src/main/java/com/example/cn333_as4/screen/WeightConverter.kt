package com.example.cn333_as4.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cn333_as4.R
import com.example.cn333_as4.viewmodels.WeightViewModel


@Composable
fun WeightConverter() {
    val viewModel: WeightViewModel = viewModel()
    val strKilo = stringResource(id = R.string.kilo)
    val strPound = stringResource(id = R.string.pound)
    val currentValue = viewModel.weight.observeAsState(viewModel.weight.value ?: "")
    val wei = viewModel.wei.observeAsState(viewModel.wei.value ?: R.string.kilo)
    var result by rememberSaveable { mutableStateOf("") }

    val enabled by remember(currentValue.value) {
        mutableStateOf(!viewModel.getWeightAsFloat().isNaN())
    }
    val calc = {
        val temp = viewModel.convert()
        result = if (temp.isNaN())
            ""
        else
            "$temp${
                if (wei.value == R.string.kilo)
                    strPound
                else strKilo
            }"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Weight",
            style = MaterialTheme.typography.h5 ,
            modifier = Modifier.padding(bottom = 16.dp),
        )
        WeightTextField(
            temperature = currentValue,
            modifier = Modifier.padding(bottom = 16.dp),
            callback = calc,
            viewModel = viewModel
        )
        WeightButtonGroup(
            selected = wei,
            modifier = Modifier.padding(bottom = 16.dp)
        ) { resId: Int -> viewModel.setWei(resId) }
        Button(
            onClick = calc,
            enabled = enabled
        ) {
            Text(text = stringResource(id = R.string.convert))
        }
        if (result.isNotEmpty()) {
            Text(
                text = result,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

@Composable
fun WeightTextField(
    temperature: State<String>,
    modifier: Modifier = Modifier,
    callback: () -> Unit,
    viewModel: WeightViewModel
) {
    TextField(
        value = temperature.value,
        onValueChange = {
            viewModel.setWeight(it)
        },
        placeholder = {
            Text(text = stringResource(id = R.string.placeholder_weight))
        },
        modifier = modifier,
        keyboardActions = KeyboardActions(onAny = {
            callback()
        }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}

@Composable
fun WeightButtonGroup(
    selected: State<Int>,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    val sel = selected.value
    Row(modifier = modifier) {
        WeightRadioButton(
            selected = sel == R.string.kilo,
            resId = R.string.kilo,
            onClick = onClick
        )
        WeightRadioButton(
            selected = sel == R.string.pound,
            resId = R.string.pound,
            onClick = onClick,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun  WeightRadioButton(
    selected: Boolean,
    resId: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        RadioButton(
            selected = selected,
            onClick = {
                onClick(resId)
            }
        )
        Text(
            text = stringResource(resId),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}