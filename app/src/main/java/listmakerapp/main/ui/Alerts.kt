package listmakerapp.main.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun RemoveConfirmationAlert(
    title: String,
    onDismissive: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        onDismissRequest = {
            onDismissive()
        },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(onClick = { onDismissive() }) {
                Text(text = "Cancel")
            }
        },
        icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = "Confirm Delete")}
    )
}