package jp.co.nissan.ae.quickothello.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.nissan.ae.quickothello.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OthelloViewModel : ViewModel() {
    private val gameLogic = OthelloGameLogic()
    private val computerPlayer = ComputerPlayer(gameLogic)

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

            // Prevent moves during computer's turn
            if (currentState.gameMode == GameMode.HUMAN_VS_COMPUTER &&
                currentState.game.currentPlayer == Player.WHITE) {
                return@launch
            }

            gameLogic.makeMove(currentState.game, row, col)?.let { newGame ->
                Log.d("OthelloViewModel", "Move successful, updating game state")
                _uiState.value = currentState.copy(
                    game = newGame,
                    showInvalidMoveMessage = false
                )
                updateValidMoves()

                // Check if it's computer's turn
                if (currentState.gameMode == GameMode.HUMAN_VS_COMPUTER &&
                    newGame.currentPlayer == Player.WHITE &&
                    newGame.gameState == GameState.ONGOING) {
                    makeComputerMove()
                }
            } ?: run {
                // Invalid move
                Log.d("OthelloViewModel", "Invalid move attempted")
                _uiState.value = currentState.copy(showInvalidMoveMessage = true)
            }
        }
    }

    private fun makeComputerMove() {
        viewModelScope.launch {
            val currentState = _uiState.value

            // Set thinking state to true
            _uiState.value = currentState.copy(isComputerThinking = true)

            // Add a small delay to make the computer move visible
            delay(500)

            // Calculate best move
            val bestMove = computerPlayer.calculateBestMove(currentState.game)

            if (bestMove != null) {
                gameLogic.makeMove(currentState.game, bestMove.row, bestMove.col)?.let { newGame ->
                    Log.d("OthelloViewModel", "Computer move: (${bestMove.row}, ${bestMove.col})")
                    _uiState.value = currentState.copy(
                        game = newGame,
                        showInvalidMoveMessage = false,
                        isComputerThinking = false
                    )
                    updateValidMoves()
                }
            } else {
                // No valid move, just clear thinking state
                _uiState.value = currentState.copy(isComputerThinking = false)
            }
        }
    }

    fun resetGame(boardSize: BoardSize? = null, gameMode: GameMode? = null) {
        Log.d("OthelloViewModel", "Resetting game with board size: ${boardSize?.displayName() ?: "current"}")
        val newBoardSize = boardSize ?: _uiState.value.selectedBoardSize
        val newGameMode = gameMode ?: _uiState.value.gameMode

        _uiState.value = OthelloUiState(
            game = OthelloGame.createInitialGame(newBoardSize),
            selectedBoardSize = newBoardSize,
            gameMode = newGameMode,
            isComputerThinking = false
        )
        updateValidMoves()
    }

    fun updateBoardSize(boardSize: BoardSize) {
        Log.d("OthelloViewModel", "Updating board size to: ${boardSize.displayName()}")
        _uiState.value = _uiState.value.copy(selectedBoardSize = boardSize)
    }

    fun updateGameMode(gameMode: GameMode) {
        Log.d("OthelloViewModel", "Updating game mode to: ${gameMode.displayName()}")
        _uiState.value = _uiState.value.copy(gameMode = gameMode)
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
    val selectedBoardSize: BoardSize = BoardSize.EIGHT,
    val gameMode: GameMode = GameMode.HUMAN_VS_HUMAN,
    val isComputerThinking: Boolean = false
)