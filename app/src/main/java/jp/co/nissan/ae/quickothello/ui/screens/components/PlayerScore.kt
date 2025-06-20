package jp.co.nissan.ae.quickothello.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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

@Composable
fun PlayerScore(
    color: Color,
    score: Int,
    isCurrentPlayer: Boolean,
    label: String,
    diskSize: androidx.compose.ui.unit.Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    hasBorder: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(diskSize)
                .clip(CircleShape)
                .background(color)
                .then(
                    if (hasBorder) Modifier.border(1.dp, Color.Black, CircleShape)
                    else Modifier
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = score.toString(),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
        if (isCurrentPlayer) {
            Text(
                text = "Your Turn",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}