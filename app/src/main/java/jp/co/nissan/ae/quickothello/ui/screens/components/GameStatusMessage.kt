package jp.co.nissan.ae.quickothello.ui.screens.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import jp.co.nissan.ae.quickothello.model.GameState

@Composable
fun GameStatusMessage(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    Text(
        text = when (gameState) {
            GameState.BLACK_WINS -> "Black Wins!"
            GameState.WHITE_WINS -> "White Wins!"
            GameState.DRAW -> "It's a Draw!"
            else -> ""
        },
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}