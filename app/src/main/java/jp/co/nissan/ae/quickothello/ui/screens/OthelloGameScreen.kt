package jp.co.nissan.ae.quickothello.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.nissan.ae.quickothello.model.CellState
import jp.co.nissan.ae.quickothello.model.GameState
import jp.co.nissan.ae.quickothello.model.OthelloGame
import jp.co.nissan.ae.quickothello.model.Player
import jp.co.nissan.ae.quickothello.model.Position
import jp.co.nissan.ae.quickothello.viewmodel.OthelloViewModel

@Composable
fun OthelloGameScreen(
    modifier: Modifier = Modifier,
    viewModel: OthelloViewModel = OthelloViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Othello",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Score Board
        ScoreBoard(
            blackScore = uiState.game.blackScore,
            whiteScore = uiState.game.whiteScore,
            currentPlayer = uiState.game.currentPlayer,
            gameState = uiState.game.gameState
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Game Board
        GameBoard(
            game = uiState.game,
            validMoves = uiState.validMoves,
            onCellClick = viewModel::onCellClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Reset Button
        Button(
            onClick = viewModel::resetGame,
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("New Game")
        }

        // Invalid Move Message
        if (uiState.showInvalidMoveMessage) {
            AlertDialog(
                onDismissRequest = viewModel::dismissInvalidMoveMessage,
                confirmButton = {
                    TextButton(onClick = viewModel::dismissInvalidMoveMessage) {
                        Text("OK")
                    }
                },
                title = { Text("Invalid Move") },
                text = { Text("You cannot place a disk here. Choose a position that will flip at least one opponent's disk.") }
            )
        }
    }
}

@Composable
fun ScoreBoard(
    blackScore: Int,
    whiteScore: Int,
    currentPlayer: Player,
    gameState: GameState
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = blackScore.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (currentPlayer == Player.BLACK && gameState == GameState.ONGOING) {
                        Text(
                            text = "Your Turn",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // VS Text
                Text(
                    text = "VS",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                // White Score
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color.Black, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = whiteScore.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (currentPlayer == Player.WHITE && gameState == GameState.ONGOING) {
                        Text(
                            text = "Your Turn",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
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
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun GameBoard(
    game: OthelloGame,
    validMoves: List<Position>,
    onCellClick: (Int, Int) -> Unit
) {
    val boardColor = Color(0xFF2E7D32) // Green board color

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = boardColor)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            for (row in 0..7) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (col in 0..7) {
                        BoardCell(
                            cellState = game.board[row][col],
                            isValidMove = validMoves.any { it.row == row && it.col == col },
                            onClick = { onCellClick(row, col) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BoardCell(
    cellState: CellState,
    isValidMove: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(2.dp)
            .clip(MaterialTheme.shapes.small)
            .background(Color(0xFF3E8E41))
            .border(
                width = 1.dp,
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
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                )
            }
            CellState.WHITE -> {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
                )
            }
            CellState.EMPTY -> {
                if (isValidMove) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
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