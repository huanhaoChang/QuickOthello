package jp.co.nissan.ae.quickothello.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.nissan.ae.quickothello.model.GameState
import jp.co.nissan.ae.quickothello.model.Player

@Composable
fun ScoreBoard(
    blackScore: Int,
    whiteScore: Int,
    currentPlayer: Player,
    gameState: GameState,
    isLandscape: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Black Score
                PlayerScore(
                    color = Color.Black,
                    score = blackScore,
                    isCurrentPlayer = currentPlayer == Player.BLACK && gameState == GameState.ONGOING,
                    label = "Black",
                    diskSize = if (isLandscape) 25.dp else 30.dp,
                    fontSize = if (isLandscape) 20.sp else 24.sp
                )

                // VS Text
                Text(
                    text = "VS",
                    fontSize = if (isLandscape) 16.sp else 20.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                // White Score
                PlayerScore(
                    color = Color.White,
                    score = whiteScore,
                    isCurrentPlayer = currentPlayer == Player.WHITE && gameState == GameState.ONGOING,
                    label = "White",
                    diskSize = if (isLandscape) 25.dp else 30.dp,
                    fontSize = if (isLandscape) 20.sp else 24.sp,
                    hasBorder = true
                )
            }

            // Game Status
            if (gameState != GameState.ONGOING) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = when (gameState) {
                        GameState.BLACK_WINS -> "Black Wins!"
                        GameState.WHITE_WINS -> "White Wins!"
                        GameState.DRAW -> "It's a Draw!"
                        else -> ""
                    },
                    fontSize = if (isLandscape) 18.sp else 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}