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
import jp.co.nissan.ae.quickothello.ui.screens.components.DrawerContent
import jp.co.nissan.ae.quickothello.ui.screens.components.GameBoard
import jp.co.nissan.ae.quickothello.ui.screens.components.InvalidMoveDialog
import jp.co.nissan.ae.quickothello.ui.screens.components.ScoreBoard
import kotlin.math.roundToInt

@Composable
fun LandscapeLayout(
    modifier: Modifier,
    uiState: jp.co.nissan.ae.quickothello.viewmodel.OthelloUiState,
    onCellClick: (Int, Int) -> Unit,
    onResetGame: () -> Unit,
    onDismissInvalidMove: () -> Unit
) {
    var isDrawerOpen by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val drawerHeight = 150.dp
    val drawerHeightPx = with(density) { drawerHeight.toPx() }

    val offsetY by animateFloatAsState(
        targetValue = if (isDrawerOpen) 0f else -drawerHeightPx,
        animationSpec = tween(durationMillis = 300),
        label = "drawer_offset"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Main Content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left side - Game Board
            BoxWithConstraints(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Menu Icon
                    IconButton(
                        onClick = { isDrawerOpen = !isDrawerOpen },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    val boardSize = minOf(this@BoxWithConstraints.maxWidth, this@BoxWithConstraints.maxHeight - 80.dp, 500.dp)
                    GameBoard(
                        game = uiState.game,
                        validMoves = uiState.validMoves,
                        onCellClick = onCellClick,
                        boardSize = boardSize
                    )
                }
            }

            // Right side - Score Board
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ScoreBoard(
                    blackScore = uiState.game.blackScore,
                    whiteScore = uiState.game.whiteScore,
                    currentPlayer = uiState.game.currentPlayer,
                    gameState = uiState.game.gameState,
                    isLandscape = true
                )
            }
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
                onResetGame = {
                    onResetGame()
                    isDrawerOpen = false
                },
                isVertical = true
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

        // Invalid Move Message
        if (uiState.showInvalidMoveMessage) {
            InvalidMoveDialog(onDismiss = onDismissInvalidMove)
        }
    }
}