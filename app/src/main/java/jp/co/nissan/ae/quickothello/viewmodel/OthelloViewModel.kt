package jp.co.nissan.ae.quickothello.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.nissan.ae.quickothello.model.*
import jp.co.nissan.ae.quickothello.repository.GameRepository
import jp.co.nissan.ae.quickothello.repository.PreferencesRepository
import jp.co.nissan.ae.quickothello.usecase.ComputerMoveUseCase
import jp.co.nissan.ae.quickothello.usecase.GetValidMovesUseCase
import jp.co.nissan.ae.quickothello.usecase.MakeMoveUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OthelloViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val preferencesRepository: PreferencesRepository,
    private val makeMoveUseCase: MakeMoveUseCase,
    private val computerMoveUseCase: ComputerMoveUseCase,
    private val getValidMovesUseCase: GetValidMovesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OthelloUiState())
    val uiState: StateFlow<OthelloUiState> = _uiState.asStateFlow()

    private var isFirstResume = true

    init {
        // Load saved preferences
        val savedBoardSize = preferencesRepository.getSavedBoardSize()
        val savedGameMode = preferencesRepository.getSavedGameMode()

        _uiState.value = OthelloUiState(
            game = gameRepository.createNewGame(savedBoardSize),
            selectedBoardSize = savedBoardSize,
            gameMode = savedGameMode
        )

        updateValidMoves()
    }

    fun onCellClick(row: Int, col: Int) {
        Log.d("OthelloViewModel", "Cell clicked at ($row, $col)")
        viewModelScope.launch {
            val currentState = _uiState.value

            when (val result = makeMoveUseCase.execute(
                currentGame = currentState.game,
                row = row,
                col = col,
                gameMode = currentState.gameMode
            )) {
                is MakeMoveUseCase.Result.Success -> {
                    Log.d("OthelloViewModel", "Move successful, updating game state")
                    val showDialog = result.newGame.gameState != GameState.ONGOING
                    _uiState.value = currentState.copy(
                        game = result.newGame,
                        showInvalidMoveMessage = false,
                        showGameOverDialog = showDialog
                    )
                    updateValidMoves()

                    if (result.shouldComputerPlay) {
                        makeComputerMove()
                    }
                }
                MakeMoveUseCase.Result.InvalidMove -> {
                    Log.d("OthelloViewModel", "Invalid move attempted")
                    _uiState.value = currentState.copy(showInvalidMoveMessage = true)
                }
                MakeMoveUseCase.Result.GameNotOngoing -> {
                    Log.d("OthelloViewModel", "Game is not ongoing")
                }
                MakeMoveUseCase.Result.NotPlayerTurn -> {
                    Log.d("OthelloViewModel", "Not player's turn")
                }
            }
        }
    }

    private fun makeComputerMove() {
        viewModelScope.launch {
            val currentState = _uiState.value

            // Set thinking state to true
            _uiState.value = currentState.copy(isComputerThinking = true)

            // Execute computer move
            val newGame = computerMoveUseCase.execute(currentState.game)

            if (newGame != null) {
                Log.d("OthelloViewModel", "Computer made a move")
                val showDialog = newGame.gameState != GameState.ONGOING
                _uiState.value = currentState.copy(
                    game = newGame,
                    showInvalidMoveMessage = false,
                    isComputerThinking = false,
                    showGameOverDialog = showDialog,
                    hasGameInProgress = newGame.gameState == GameState.ONGOING
                )
                updateValidMoves()
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
            game = gameRepository.createNewGame(newBoardSize),
            selectedBoardSize = newBoardSize,
            gameMode = newGameMode,
            isComputerThinking = false,
            hasGameInProgress = true
        )
        updateValidMoves()
    }

    fun updateBoardSize(boardSize: BoardSize) {
        Log.d("OthelloViewModel", "Updating board size to: ${boardSize.displayName()}")
        _uiState.value = _uiState.value.copy(selectedBoardSize = boardSize)
        preferencesRepository.saveBoardSize(boardSize)
    }

    fun updateGameMode(gameMode: GameMode) {
        Log.d("OthelloViewModel", "Updating game mode to: ${gameMode.displayName()}")
        _uiState.value = _uiState.value.copy(gameMode = gameMode)
        preferencesRepository.saveGameMode(gameMode)
    }

    fun dismissInvalidMoveMessage() {
        _uiState.value = _uiState.value.copy(showInvalidMoveMessage = false)
    }

    fun dismissGameOverDialog() {
        _uiState.value = _uiState.value.copy(showGameOverDialog = false)
    }

    fun onAppPaused() {
        Log.d("OthelloViewModel", "App paused")
        // Set flag to indicate app has been used
        isFirstResume = false
    }

    fun onAppResumed() {
        Log.d("OthelloViewModel", "App resumed, isFirstResume: $isFirstResume")
        if (isFirstResume) {
            // Skip showing dialog on first resume (app start)
            isFirstResume = false
        } else {
            val currentState = _uiState.value
            // Show resume dialog only if there's a game in progress and it's not game over
            if (currentState.hasGameInProgress && currentState.game.gameState == GameState.ONGOING) {
                // Check if the game has actually progressed (not just initial state)
                val isGameProgressed = currentState.game.blackScore > 2 || currentState.game.whiteScore > 2
                if (isGameProgressed) {
                    _uiState.value = currentState.copy(showResumeDialog = true)
                }
            }
        }
    }

    fun onResumeGame() {
        Log.d("OthelloViewModel", "Resuming game")
        _uiState.value = _uiState.value.copy(showResumeDialog = false)
    }

    fun onNewGameFromResume() {
        Log.d("OthelloViewModel", "Starting new game from resume dialog")
        _uiState.value = _uiState.value.copy(showResumeDialog = false)
        resetGame()
    }

    private fun updateValidMoves() {
        val currentGame = _uiState.value.game
        val validMoves = getValidMovesUseCase.execute(currentGame)
        Log.d("OthelloViewModel", "Valid moves count: ${validMoves.size}")
        _uiState.value = _uiState.value.copy(validMoves = validMoves)
    }
}