package jp.co.nissan.ae.quickothello.usecase

import jp.co.nissan.ae.quickothello.model.OthelloGame
import jp.co.nissan.ae.quickothello.model.Position
import jp.co.nissan.ae.quickothello.repository.GameRepository
import javax.inject.Inject

class GetValidMovesUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    fun execute(game: OthelloGame): List<Position> {
        return gameRepository.getValidMoves(game, game.currentPlayer)
    }
}