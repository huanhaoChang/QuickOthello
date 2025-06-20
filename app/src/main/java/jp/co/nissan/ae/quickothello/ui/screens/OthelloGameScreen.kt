package jp.co.nissan.ae.quickothello.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.co.nissan.ae.quickothello.viewmodel.OthelloViewModel

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