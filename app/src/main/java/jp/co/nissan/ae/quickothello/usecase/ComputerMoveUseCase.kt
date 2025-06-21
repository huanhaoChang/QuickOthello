package jp.co.nissan.ae.quickothello.usecase

import jp.co.nissan.ae.quickothello.model.OthelloGame
import jp.co.nissan.ae.quickothello.repository.GameRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class ComputerMoveUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute(currentGame: OthelloGame): OthelloGame? {
        // Add a small delay to make the computer move visible
        delay(500)

        // Calculate the best move
        val bestMove = gameRepository.calculateComputerMove(currentGame)

        // Make the move if one was found
        return bestMove?.let { position ->
            gameRepository.makeMove(currentGame, position.row, position.col)
        }
    }
}