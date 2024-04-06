package ir.amirreza.module7.features.add_or_update

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.amirreza.module7.components.DropDownTextField
import ir.amirreza.module7.data.states.LocalAppState
import ir.amirreza.module7.utils.checkInternet
import ir.amirreza.module7.viewmodels.add_or_update.AddOrUpdateViewModel

@Composable
fun AddOrUpdateScreen(id: Int) {
    val appState = LocalAppState.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val internetAvailable by checkInternet(
        context,
        scope
    ).collectAsStateWithLifecycle(initialValue = false)
    val viewModel: AddOrUpdateViewModel = viewModel()
    val wellName = viewModel.wellName
    val gasOilDepth = viewModel.gasOilDepth
    val capacity = viewModel.capacity
    val rockType = viewModel.rockType
    val fromDepth = viewModel.fromDepth
    val toDepth = viewModel.toDepth
    val rockTypes by viewModel.rockTypes.collectAsStateWithLifecycle()
    var expandedDropDownRock by remember {
        mutableStateOf(false)
    }
    val layers by viewModel.layers.collectAsStateWithLifecycle()
    val message = viewModel.message
    LaunchedEffect(key1 = message) {
        if (message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.message = ""
        }
    }
    LaunchedEffect(key1 = id) {
        if (id != 0) {
            viewModel.initId(id)
        }
    }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(value = wellName, onValueChange = {
            viewModel.wellName = it
        }, placeholder = {
            Text(text = "Well Name")
        }, modifier = Modifier.fillMaxWidth())
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = gasOilDepth,
                onValueChange = {
                    viewModel.gasOilDepth = it
                },
                placeholder = {
                    Text(
                        text = "Depth of Gas or Oil Extraction",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.weight(.6f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(value = capacity, onValueChange = {
                viewModel.capacity = it
            }, placeholder = {
                Text(text = "Well Capacity")
            }, modifier = Modifier.weight(.4f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Text(text = "Rock Layers:", modifier = Modifier.padding(top = 8.dp))
        HorizontalDivider()
        DropDownTextField(
            expanded = expandedDropDownRock,
            value = rockType?.name ?: "",
            placeholder = "Rock Layer",
            items = rockTypes,
            onExpandedChanged = { expandedDropDownRock = it },
            onSelect = {
                viewModel.rockType = it
                expandedDropDownRock = false
            }
        ) {
            Text(text = it.name)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = fromDepth, onValueChange = {
                viewModel.fromDepth = it
            }, placeholder = {
                Text(
                    text = "From Depth",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }, modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(value = toDepth, onValueChange = {
                viewModel.toDepth = it
            }, placeholder = {
                Text(
                    text = "To Depth",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }, modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextButton(onClick = viewModel::addLayer, modifier = Modifier.weight(1f)) {
                Text(text = "Add Layer")
            }
        }
        layers.forEachIndexed { index, layer ->
            ListItem(headlineContent = { Text(text = layer.rockType.name) }, leadingContent = {
                IconButton(onClick = {
                    viewModel.deleteLayer(index)
                }) {
                    Icon(imageVector = Icons.Rounded.Clear, contentDescription = null)
                }
            }, supportingContent = {
                Text(text = "From: ${layer.layer.startPoint} to ${layer.layer.endPoint}")
            })
        }
        Button(onClick = {
            viewModel.submit {
                appState.navigation.popBackStack()
            }
        }, enabled = internetAvailable, modifier = Modifier.align(Alignment.End)) {
            Text(text = "Submit")
        }
    }
}