package ir.amirreza.module7.features.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.amirreza.module7.data.states.LocalAppState
import ir.amirreza.module7.utils.checkInternet
import ir.amirreza.module7.viewmodels.home.HomeViewModel

@Composable
fun HomeScreen() {
    val viewModel = viewModel(modelClass = HomeViewModel::class.java)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val internetAvailable by checkInternet(
        context,
        scope
    ).collectAsStateWithLifecycle(initialValue = false)
    var expandedWellSelect by remember {
        mutableStateOf(false)
    }
    val data by viewModel.data.collectAsStateWithLifecycle()
    val wells by viewModel.wells.collectAsStateWithLifecycle()
    val appState = LocalAppState.current
    val navigation = appState.navigation
    Box(contentAlignment = Alignment.BottomEnd) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = "Well name:")
                Box {
                    TextButton(onClick = { expandedWellSelect = true }) {
                        Text(text = data?.well?.wellName ?: "")
                    }
                    DropDown(
                        expandedWellSelect,
                        wells,
                        {
                            expandedWellSelect = false
                        },
                        {
                            expandedWellSelect = false
                            viewModel.getData(it.wellId)
                        }
                    ) {
                        Text(text = it.wellName)
                    }
                }
                Button(onClick = {
                    if (data != null) {
                        if (internetAvailable) {
                            navigation.navigate("add_or_update?id=${data!!.well.wellId}")
                        } else {
                            Toast.makeText(
                                context,
                                "Internet is not available!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }) {
                    Text(text = "Edit")
                }
            }
            BoxWithConstraints(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .weight(1f)
            ) {
                val height = maxHeight
                Column(
                    modifier = Modifier.padding(horizontal = 36.dp)
                ) {
                    data?.let { requireData ->
                        requireData.layers.forEach { layer ->
                            val percent =
                                layer.layer.endPoint.minus(layer.layer.startPoint) / requireData.well.gasOilDepth.toFloat()
                            key(layer.layer.wellLayerId) {
                                Row {
                                    Box(
                                        modifier = Modifier
                                            .background(layer.rockType.getComposeColor())
                                            .weight(1f)
                                            .height(height * percent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = layer.rockType.name, color = Color.Black)
                                    }
                                    Text(
                                        text = "${layer.layer.startPoint} m",
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                            .width(60.dp),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Row {
                Text(
                    buildAnnotatedString {
                        append("Well capacity: ")
                        data?.well?.capacity?.let { capacity ->
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("$capacity")
                            }
                            append(" m3")
                        }
                    }
                )
            }
        }
        FloatingActionButton(
            onClick = {
                if (internetAvailable) {
                    navigation.navigate("add_or_update?id=0")
                } else {
                    Toast.makeText(context, "Internet is not available!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(16.dp),
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
        }
    }
}

@Composable
fun <T> DropDown(
    expanded: Boolean,
    items: List<T>,
    onDismissRequest: () -> Unit,
    onSelect: (T) -> Unit,
    content: @Composable (T) -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        items.forEach {
            DropdownMenuItem(text = { content.invoke(it) }, onClick = { onSelect.invoke(it) })
        }
    }
}
