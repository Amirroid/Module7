package ir.amirreza.module7.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownTextField(
    expanded: Boolean,
    value: String,
    placeholder: String,
    items: List<T>,
    onExpandedChanged: (Boolean) -> Unit,
    onSelect: (T) -> Unit,
    content: @Composable (T) -> Unit
) {
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChanged) {
        OutlinedTextField(value = value, onValueChange = {}, readOnly = true, placeholder = {
            Text(text = placeholder)
        }, modifier = Modifier
            .fillMaxWidth()
            .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChanged.invoke(false) }) {
            items.forEach {
                DropdownMenuItem(text = { content.invoke(it) }, onClick = { onSelect.invoke(it) })
            }
        }
    }
}