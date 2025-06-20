package jp.co.nissan.ae.quickothello.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.nissan.ae.quickothello.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OthelloViewModel : ViewModel() {
    private val gameLogic = OthelloGameLogic()

    private val _uiState = MutableStateFlow(OthelloUiState())
    val uiState: StateFlow<OthelloUiState> = _uiState.asStateFlow()

    init {
        updateValidMoves()
    }

    fun onCellClick(row: Int, col: Int) {
        Log.d("OthelloViewModel", "Cell clicked at ($row, $col)")
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.game.gameState != GameState.ONGOING) return@launch

            gameLogic.makeMove(currentState.game, row, col)?.let { newGame ->
                Log.d("OthelloViewModel", "Move successful, updating game state")
                _uiState.value = currentState.copy(
                    game = newGame,
                    showInvalidMoveMessage = false
                )
                updateValidMoves()
            } ?: run {
                // Invalid move
                Log.d("OthelloViewModel", "Invalid move attempted")
                _uiState.value = currentState.copy(showInvalidMoveMessage = true)
            }
        }
    }

    fun resetGame(boardSize: BoardSize? = null) {
        Log.d("OthelloViewModel", "Resetting game with board size: ${boardSize?.displayName() ?: "current"}")
        val newBoardSize = boardSize ?: _uiState.value.selectedBoardSize
        _uiState.value = OthelloUiState(
            game = OthelloGame.createInitialGame(newBoardSize),
            selectedBoardSize = newBoardSize
        )
        updateValidMoves()
    }

    fun updateBoardSize(boardSize: BoardSize) {
        Log.d("OthelloViewModel", "Updating board size to: ${boardSize.displayName()}")
        _uiState.value = _uiState.value.copy(selectedBoardSize = boardSize)
    }

    fun dismissInvalidMoveMessage() {
        _uiState.value = _uiState.value.copy(showInvalidMoveMessage = false)
    }

    private fun updateValidMoves() {
        val currentGame = _uiState.value.game
        val validMoves = gameLogic.getValidMoves(currentGame, currentGame.currentPlayer)
        Log.d("OthelloViewModel", "Valid moves count: ${validMoves.size}")
        _uiState.value = _uiState.value.copy(validMoves = validMoves)
    }
}

data class OthelloUiState(
    val game: OthelloGame = OthelloGame.createInitialGame(),
    val validMoves: List<Position> = emptyList(),
    val showInvalidMoveMessage: Boolean = false,
    val selectedBoardSize: BoardSize = BoardSize.EIGHT
)