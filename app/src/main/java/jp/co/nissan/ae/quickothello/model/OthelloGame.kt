package jp.co.nissan.ae.quickothello.model

data class OthelloGame(
    val board: Array<Array<CellState>>,
    val currentPlayer: Player = Player.BLACK,
    val gameState: GameState = GameState.ONGOING,
    val blackScore: Int = 2,
    val whiteScore: Int = 2
) {
    companion object {
        fun createInitialGame(): OthelloGame {
            val board = Array(8) { Array(8) { CellState.EMPTY } }
            // Set initial board position
            board[3][3] = CellState.WHITE
            board[3][4] = CellState.BLACK
            board[4][3] = CellState.BLACK
            board[4][4] = CellState.WHITE

            return OthelloGame(
                board = board,
                currentPlayer = Player.BLACK,
                gameState = GameState.ONGOING,
                blackScore = 2,
                whiteScore = 2
            )
        }
    }

    fun copy(): OthelloGame {
        return OthelloGame(
            board = Array(8) { row -> Array(8) { col -> board[row][col] } },
            currentPlayer = currentPlayer,
            gameState = gameState,
            blackScore = blackScore,
            whiteScore = whiteScore
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OthelloGame) return false

        if (!board.contentDeepEquals(other.board)) return false
        if (currentPlayer != other.currentPlayer) return false
        if (gameState != other.gameState) return false
        if (blackScore != other.blackScore) return false
        if (whiteScore != other.whiteScore) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.contentDeepHashCode()
        result = 31 * result + currentPlayer.hashCode()
        result = 31 * result + gameState.hashCode()
        result = 31 * result + blackScore
        result = 31 * result + whiteScore
        return result
    }
}