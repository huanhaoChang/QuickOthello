package jp.co.nissan.ae.quickothello.repository

import jp.co.nissan.ae.quickothello.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val gameLogic: GameLogic,
    private val computerPlayer: ComputerPlayer
) {

    fun createNewGame(boardSize: BoardSize): OthelloGame {
        return OthelloGame.createInitialGame(boardSize)
    }

    fun makeMove(game: OthelloGame, row: Int, col: Int): OthelloGame? {
        return gameLogic.makeMove(game, row, col)
    }

    fun getValidMoves(game: OthelloGame, player: Player): List<Position> {
        return gameLogic.getValidMoves(game, player)
    }

    fun isValidMove(game: OthelloGame, row: Int, col: Int, player: Player): Boolean {
        return gameLogic.isValidMove(game, row, col, player)
    }

    suspend fun calculateComputerMove(game: OthelloGame): Position? {
        return computerPlayer.calculateBestMove(game)
    }
}