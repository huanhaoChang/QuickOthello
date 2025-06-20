package jp.co.nissan.ae.quickothello.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jp.co.nissan.ae.quickothello.model.OthelloGame
import jp.co.nissan.ae.quickothello.model.Position

@Composable
fun GameBoard(
    game: OthelloGame,
    validMoves: List<Position>,
    onCellClick: (Int, Int) -> Unit,
    boardSize: androidx.compose.ui.unit.Dp
) {
    val boardColor = Color(0xFF2E7D32) // Green board color
    val cellSize = (boardSize - 16.dp) / 8

    Card(
        modifier = Modifier.size(boardSize),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = boardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0..7) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (col in 0..7) {
                        BoardCell(
                            cellState = game.board[row][col],
                            isValidMove = validMoves.any { it.row == row && it.col == col },
                            onClick = { onCellClick(row, col) },
                            cellSize = cellSize
                        )
                    }
                }
            }
        }
    }
}