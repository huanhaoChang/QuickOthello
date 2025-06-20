package jp.co.nissan.ae.quickothello.ui.screens.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun InvalidMoveDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = { Text("Invalid Move") },
        text = { Text("You cannot place a disk here. Choose a position that will flip at least one opponent's disk.") }
    )
}