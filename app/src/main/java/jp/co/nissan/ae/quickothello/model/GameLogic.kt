package jp.co.nissan.ae.quickothello.model

interface GameLogic {
    fun makeMove(game: OthelloGame, row: Int, col: Int): OthelloGame?
    fun isValidMove(game: OthelloGame, row: Int, col: Int, player: Player): Boolean
    fun getValidMoves(game: OthelloGame, player: Player): List<Position>
}