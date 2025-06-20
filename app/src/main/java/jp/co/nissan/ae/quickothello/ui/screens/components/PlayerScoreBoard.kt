package jp.co.nissan.ae.quickothello.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.nissan.ae.quickothello.model.Player

@Composable
fun PlayerScoreBoard(
    player: Player,
    score: Int,
    isCurrentPlayer: Boolean,
    isHorizontal: Boolean,
    modifier: Modifier = Modifier
) {
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
                    text = if (player == Player.BLACK) "Black" else "White",
                    fontSize = 18.sp,
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
                    text = score.toString(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentPlayer)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                // Current turn indicator
                if (isCurrentPlayer) {
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = "Your Turn",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
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
                    text = if (player == Player.BLACK) "Black" else "White",
                    fontSize = 20.sp,
                    fontWeight = if (isCurrentPlayer) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrentPlayer)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentPlayer)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                // Current turn indicator
                if (isCurrentPlayer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your Turn",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}