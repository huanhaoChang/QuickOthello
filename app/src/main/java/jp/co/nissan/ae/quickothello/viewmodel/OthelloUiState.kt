package jp.co.nissan.ae.quickothello.viewmodel

import jp.co.nissan.ae.quickothello.model.BoardSize
import jp.co.nissan.ae.quickothello.model.GameMode
import jp.co.nissan.ae.quickothello.model.OthelloGame
import jp.co.nissan.ae.quickothello.model.Position

data class OthelloUiState(
    val game: OthelloGame = OthelloGame.createInitialGame(),
    val validMoves: List<Position> = emptyList(),
    val showInvalidMoveMessage: Boolean = false,
    val showGameOverDialog: Boolean = false,
    val showResumeDialog: Boolean = false,
    val selectedBoardSize: BoardSize = BoardSize.EIGHT,
    val gameMode: GameMode = GameMode.HUMAN_VS_HUMAN,
    val isComputerThinking: Boolean = false,
    val hasGameInProgress: Boolean = false
)