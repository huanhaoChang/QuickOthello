package jp.co.nissan.ae.quickothello.ui.screens

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import jp.co.nissan.ae.quickothello.viewmodel.OthelloViewModel

@Composable
fun OthelloGameScreen(
    modifier: Modifier = Modifier,
    viewModel: OthelloViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        LandscapeLayout(
            modifier = modifier,
            uiState = uiState,
            onCellClick = viewModel::onCellClick,
            onResetGame = { viewModel.resetGame(uiState.selectedBoardSize, uiState.gameMode) },
            onBoardSizeSelected = viewModel::updateBoardSize,
            onGameModeSelected = viewModel::updateGameMode,
            onDismissInvalidMove = viewModel::dismissInvalidMoveMessage,
            onDismissGameOver = viewModel::dismissGameOverDialog,
            onResumeGame = viewModel::onResumeGame,
            onNewGameFromResume = viewModel::onNewGameFromResume
        )
    } else {
        PortraitLayout(
            modifier = modifier,
            uiState = uiState,
            onCellClick = viewModel::onCellClick,
            onResetGame = { viewModel.resetGame(uiState.selectedBoardSize, uiState.gameMode) },
            onBoardSizeSelected = viewModel::updateBoardSize,
            onGameModeSelected = viewModel::updateGameMode,
            onDismissInvalidMove = viewModel::dismissInvalidMoveMessage,
            onDismissGameOver = viewModel::dismissGameOverDialog,
            onResumeGame = viewModel::onResumeGame,
            onNewGameFromResume = viewModel::onNewGameFromResume
        )
    }
}