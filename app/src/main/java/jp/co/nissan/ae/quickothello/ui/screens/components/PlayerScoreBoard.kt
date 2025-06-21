package jp.co.nissan.ae.quickothello.ui.screens.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.nissan.ae.quickothello.model.GameMode
import jp.co.nissan.ae.quickothello.model.Player

@Composable
fun PlayerScoreBoard(
    player: Player,
    score: Int,
    isCurrentPlayer: Boolean,
    isHorizontal: Boolean,
    gameMode: GameMode,
    isComputerThinking: Boolean = false,
    modifier: Modifier = Modifier
) {
    val playerLabel = when {
        player == Player.BLACK -> "Black"
        player == Player.WHITE && gameMode == GameMode.HUMAN_VS_COMPUTER -> "White (AI)"
        else -> "White"
    }

    // Animation for thinking indicator
    val infiniteTransition = rememberInfiniteTransition(label = "thinking_animation")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "thinking_alpha"
    )

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCurrentPlayer) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentPlayer)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        )
    ) {
        if (isHorizontal) {
            // Horizontal layout for portrait mode
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Player label
                Text(
                    modifier = Modifier.weight(2f),
                    text = playerLabel,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = if (isCurrentPlayer) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrentPlayer)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(24.dp))

                // Disk
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (player == Player.BLACK) Color.Black else Color.White)
                        .then(
                            if (player == Player.WHITE)
                                Modifier.border(1.dp, Color.Black.copy(alpha = 0.3f), CircleShape)
                            else
                                Modifier
                        )
                )

                Spacer(modifier = Modifier.width(24.dp))

                // Score
                Text(
                    modifier = Modifier.weight(1f),
                    text = score.toString(),
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentPlayer)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                // Current turn indicator
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2f),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCurrentPlayer) {
                        Spacer(modifier = Modifier.width(24.dp))
                        val text = when {
                            isComputerThinking && player == Player.WHITE -> "Thinking..."
                            player == Player.WHITE && gameMode == GameMode.HUMAN_VS_COMPUTER -> "AI's Turn"
                            else -> "Your Turn"
                        }
                        Text(
                            text = text,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            modifier = if (isComputerThinking && player == Player.WHITE)
                                Modifier.alpha(alpha) else Modifier
                        )
                    }
                }
            }
        } else {
            // Vertical layout for landscape mode
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Player label
                Text(
                    text = playerLabel,
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    fontWeight = if (isCurrentPlayer) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrentPlayer)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                // Disk
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (player == Player.BLACK) Color.Black else Color.White)
                        .then(
                            if (player == Player.WHITE)
                                Modifier.border(1.dp, Color.Black.copy(alpha = 0.3f), CircleShape)
                            else
                                Modifier
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Score
                Text(
                    text = score.toString(),
                    modifier = Modifier.weight(1f),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentPlayer)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                // Current turn indicator
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCurrentPlayer) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val text = when {
                            isComputerThinking && player == Player.WHITE -> "Thinking..."
                            player == Player.WHITE && gameMode == GameMode.HUMAN_VS_COMPUTER -> "AI's Turn"
                            else -> "Your Turn"
                        }
                        Text(
                            text = text,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            modifier = if (isComputerThinking && player == Player.WHITE)
                                Modifier.alpha(alpha) else Modifier
                        )
                    }
                }
            }
        }
    }
}