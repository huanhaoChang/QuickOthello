package jp.co.nissan.ae.quickothello.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.co.nissan.ae.quickothello.ui.screens.components.DrawerContent
import jp.co.nissan.ae.quickothello.ui.screens.components.GameBoard
import jp.co.nissan.ae.quickothello.ui.screens.components.InvalidMoveDialog
import jp.co.nissan.ae.quickothello.ui.screens.components.ScoreBoard
import jp.co.nissan.ae.quickothello.viewmodel.OthelloViewModel
import kotlin.math.roundToInt

@Composable
fun OthelloGameScreen(
    modifier: Modifier = Modifier,
    viewModel: OthelloViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        LandscapeLayout(
            modifier = modifier,
            uiState = uiState,
            onCellClick = viewModel::onCellClick,
            onResetGame = viewModel::resetGame,
            onDismissInvalidMove = viewModel::dismissInvalidMoveMessage
        )
    } else {
        PortraitLayout(
            modifier = modifier,
            uiState = uiState,
            onCellClick = viewModel::onCellClick,
            onResetGame = viewModel::resetGame,
            onDismissInvalidMove = viewModel::dismissInvalidMoveMessage
        )
    }
}