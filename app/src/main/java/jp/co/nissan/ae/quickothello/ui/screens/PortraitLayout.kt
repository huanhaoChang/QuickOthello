package jp.co.nissan.ae.quickothello.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import jp.co.nissan.ae.quickothello.model.BoardSize
import jp.co.nissan.ae.quickothello.model.GameMode
import jp.co.nissan.ae.quickothello.model.GameState
import jp.co.nissan.ae.quickothello.model.Player
import jp.co.nissan.ae.quickothello.ui.screens.components.DrawerContent
import jp.co.nissan.ae.quickothello.ui.screens.components.GameBoard
import jp.co.nissan.ae.quickothello.ui.screens.components.GameStatusMessage
import jp.co.nissan.ae.quickothello.ui.screens.components.InvalidMoveDialog
import jp.co.nissan.ae.quickothello.ui.screens.components.PlayerScoreBoard
import jp.co.nissan.ae.quickothello.ui.screens.components.ResumeGameDialog
import jp.co.nissan.ae.quickothello.viewmodel.OthelloUiState
import kotlin.math.roundToInt

@Composable
fun PortraitLayout(
    modifier: Modifier,
    uiState: OthelloUiState,
    onCellClick: (Int, Int) -> Unit,
    onResetGame: () -> Unit,
    onBoardSizeSelected: (BoardSize) -> Unit,
    onGameModeSelected: (GameMode) -> Unit,
    onDismissInvalidMove: () -> Unit,
    onDismissGameOver: () -> Unit,
    onResumeGame: () -> Unit,
    onNewGameFromResume: () -> Unit
) {
    var isDrawerOpen by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val drawerWidth = 250.dp
    val drawerWidthPx = with(density) { drawerWidth.toPx() }

    val offsetX by animateFloatAsState(
        targetValue = if (isDrawerOpen) 0f else -drawerWidthPx,
        animationSpec = tween(durationMillis = 300),
        label = "drawer_offset"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Menu Icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                IconButton(
                    onClick = { isDrawerOpen = !isDrawerOpen },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Black Player Score (Top)
            PlayerScoreBoard(
                player = Player.BLACK,
                score = uiState.game.blackScore,
                isCurrentPlayer = uiState.game.currentPlayer == Player.BLACK && uiState.game.gameState == GameState.ONGOING,
                isHorizontal = true,
                gameMode = uiState.gameMode,
                isComputerThinking = uiState.isComputerThinking,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Game Board (Center)
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BoxWithConstraints {
                    val boardSize = minOf(maxWidth - 32.dp, maxHeight - 32.dp, 400.dp)
                    GameBoard(
                        game = uiState.game,
                        validMoves = uiState.validMoves,
                        onCellClick = onCellClick,
                        boardSize = boardSize
                    )
                }
            }

            // White Player Score (Bottom)
            PlayerScoreBoard(
                player = Player.WHITE,
                score = uiState.game.whiteScore,
                isCurrentPlayer = uiState.game.currentPlayer == Player.WHITE && uiState.game.gameState == GameState.ONGOING,
                isHorizontal = true,
                gameMode = uiState.gameMode,
                isComputerThinking = uiState.isComputerThinking,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        // Overlay to close drawer
        if (isDrawerOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { isDrawerOpen = false }
            )
        }

        // Horizontal Drawer (slides from left)
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .fillMaxHeight()
                .width(drawerWidth)
                .shadow(8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount > 0 && !isDrawerOpen) {
                            isDrawerOpen = true
                        } else if (dragAmount < 0 && isDrawerOpen) {
                            isDrawerOpen = false
                        }
                    }
                }
        ) {
            DrawerContent(
                selectedBoardSize = uiState.selectedBoardSize,
                selectedGameMode = uiState.gameMode,
                onBoardSizeSelected = onBoardSizeSelected,
                onGameModeSelected = onGameModeSelected,
                onResetGame = {
                    onResetGame()
                    isDrawerOpen = false
                },
                isVertical = false
            )
        }

        // Invalid Move Message
        if (uiState.showInvalidMoveMessage) {
            InvalidMoveDialog(onDismiss = onDismissInvalidMove)
        }

        // Game Over Dialog
        if (uiState.showGameOverDialog) {
            GameStatusMessage(
                gameState = uiState.game.gameState,
                blackScore = uiState.game.blackScore,
                whiteScore = uiState.game.whiteScore,
                onNewGame = onResetGame,
                onDismiss = onDismissGameOver
            )
        }

        // Resume Game Dialog
        if (uiState.showResumeDialog) {
            ResumeGameDialog(
                onResumeGame = onResumeGame,
                onNewGame = onNewGameFromResume
            )
        }
    }
}