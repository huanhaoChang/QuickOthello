package jp.co.nissan.ae.quickothello.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jp.co.nissan.ae.quickothello.model.CellState

@Composable
fun BoardCell(
    cellState: CellState,
    isValidMove: Boolean,
    onClick: () -> Unit,
    cellSize: androidx.compose.ui.unit.Dp
) {
    val diskSize = cellSize * 0.7f

    Box(
        modifier = Modifier
            .size(cellSize)
            .padding(1.dp)
            .clip(MaterialTheme.shapes.small)
            .background(Color(0xFF3E8E41))
            .border(
                width = 0.5.dp,
                color = Color.Black.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.small
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when (cellState) {
            CellState.BLACK -> {
                Box(
                    modifier = Modifier
                        .size(diskSize)
                        .clip(CircleShape)
                        .background(Color.Black)
                )
            }
            CellState.WHITE -> {
                Box(
                    modifier = Modifier
                        .size(diskSize)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(0.5.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
                )
            }
            CellState.EMPTY -> {
                if (isValidMove) {
                    Box(
                        modifier = Modifier
                            .size(diskSize)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = Color.Yellow.copy(alpha = 0.6f),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}