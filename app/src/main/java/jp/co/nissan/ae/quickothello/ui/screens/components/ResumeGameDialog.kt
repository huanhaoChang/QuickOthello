package jp.co.nissan.ae.quickothello.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResumeGameDialog(
    onResumeGame: () -> Unit,
    onNewGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onResumeGame, // Default to resume if dismissed
        icon = {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Game Icon",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "Welcome Back!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "You have a game in progress.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Would you like to continue where you left off?",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onResumeGame,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Resume Game")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onNewGame,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("New Game")
            }
        },
        modifier = modifier
    )
}