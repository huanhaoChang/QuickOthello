package jp.co.nissan.ae.quickothello.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import jp.co.nissan.ae.quickothello.model.Player
import jp.co.nissan.ae.quickothello.ui.screens.components.DrawerContent
import jp.co.nissan.ae.quickothello.ui.screens.components.GameBoard
import jp.co.nissan.ae.quickothello.ui.screens.components.GameStatusMessage
import jp.co.nissan.ae.quickothello.ui.screens.components.InvalidMoveDialog
import jp.co.nissan.ae.quickothello.ui.screens.components.PlayerScoreBoard
import kotlin.math.roundToInt

@Composable
fun LandscapeLayout(
    modifier: Modifier,
    uiState: jp.co.nissan.ae.quickothello.viewmodel.OthelloUiState,
    onCellClick: (Int, Int) -> Unit,
    onResetGame: () -> Unit,
    onBoardSizeSelected: (BoardSize) -> Unit,
    onDismissInvalidMove: () -> Unit
) {
    var isDrawerOpen by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val drawerHeight = 180.dp
    val drawerHeightPx = with(density) { drawerHeight.toPx() }

    val offsetY by animateFloatAsState(
        targetValue = if (isDrawerOpen) 0f else -drawerHeightPx,
        animationSpec = tween(durationMillis = 300),
        label = "drawer_offset"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Main Content
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Menu Icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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

                // Game Status (if game is over) - positioned at top center in landscape
                if (uiState.game.gameState != jp.co.nissan.ae.quickothello.model.GameState.ONGOING) {
                    GameStatusMessage(
                        gameState = uiState.game.gameState,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Black Player Score (Left)
                PlayerScoreBoard(
                    player = Player.BLACK,
                    score = uiState.game.blackScore,
                    isCurrentPlayer = uiState.game.currentPlayer == Player.BLACK && uiState.game.gameState == jp.co.nissan.ae.quickothello.model.GameState.ONGOING,
                    isHorizontal = false,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )

                // Game Board (Center)
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    BoxWithConstraints {
                        val boardSize = minOf(maxWidth - 16.dp, maxHeight - 16.dp, 500.dp)
                        GameBoard(
                            game = uiState.game,
                            validMoves = uiState.validMoves,
                            onCellClick = onCellClick,
                            boardSize = boardSize
                        )
                    }
                }

                // White Player Score (Right)
                PlayerScoreBoard(
                    player = Player.WHITE,
                    score = uiState.game.whiteScore,
                    isCurrentPlayer = uiState.game.currentPlayer == Player.WHITE && uiState.game.gameState == jp.co.nissan.ae.quickothello.model.GameState.ONGOING,
                    isHorizontal = false,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }

        // Overlay to close drawer
        if (isDrawerOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { isDrawerOpen = false }
            )
        }

        // Vertical Drawer (slides from top)
        Box(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.roundToInt()) }
                .fillMaxWidth()
                .height(drawerHeight)
                .shadow(8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
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
                onBoardSizeSelected = onBoardSizeSelected,
                onResetGame = {
                    onResetGame()
                    isDrawerOpen = false
                },
                isVertical = true
            )
        }

        // Invalid Move Message
        if (uiState.showInvalidMoveMessage) {
            InvalidMoveDialog(onDismiss = onDismissInvalidMove)
        }
    }
}