package jp.co.nissan.ae.quickothello.usecase

import jp.co.nissan.ae.quickothello.model.GameMode
import jp.co.nissan.ae.quickothello.model.GameState
import jp.co.nissan.ae.quickothello.model.OthelloGame
import jp.co.nissan.ae.quickothello.model.Player
import jp.co.nissan.ae.quickothello.repository.GameRepository
import javax.inject.Inject

class MakeMoveUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    sealed class Result {
        data class Success(val newGame: OthelloGame, val shouldComputerPlay: Boolean) : Result()
        object InvalidMove : Result()
        object GameNotOngoing : Result()
        object NotPlayerTurn : Result()
    }

    fun execute(
        currentGame: OthelloGame,
        row: Int,
        col: Int,
        gameMode: GameMode
    ): Result {
        // Check if game is ongoing
        if (currentGame.gameState != GameState.ONGOING) {
            return Result.GameNotOngoing
        }

        // Check if it's not computer's turn in Human vs Computer mode
        if (gameMode == GameMode.HUMAN_VS_COMPUTER && currentGame.currentPlayer == Player.WHITE) {
            return Result.NotPlayerTurn
        }

        // Try to make the move
        val newGame = gameRepository.makeMove(currentGame, row, col)

        return if (newGame != null) {
            // Check if computer should play next
            val shouldComputerPlay = gameMode == GameMode.HUMAN_VS_COMPUTER &&
                    newGame.currentPlayer == Player.WHITE &&
                    newGame.gameState == GameState.ONGOING

            Result.Success(newGame, shouldComputerPlay)
        } else {
            Result.InvalidMove
        }
    }
}