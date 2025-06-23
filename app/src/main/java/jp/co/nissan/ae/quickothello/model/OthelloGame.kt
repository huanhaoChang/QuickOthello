package jp.co.nissan.ae.quickothello.model

data class OthelloGame(
    val board: Array<Array<CellState>>,
    val boardSize: BoardSize = BoardSize.EIGHT,
    val currentPlayer: Player = Player.BLACK,
    val gameState: GameState = GameState.ONGOING,
    val blackScore: Int = 2,
    val whiteScore: Int = 2
) {
    companion object {
        fun createInitialGame(boardSize: BoardSize = BoardSize.EIGHT): OthelloGame {
            val size = boardSize.size
            val board = Array(size) { Array(size) { CellState.EMPTY } }

            // Set initial board position based on board size
            when (boardSize) {
                BoardSize.FOUR -> {
                    // For 4x4, place pieces in the center 2x2
                    board[1][1] = CellState.WHITE
                    board[1][2] = CellState.BLACK
                    board[2][1] = CellState.BLACK
                    board[2][2] = CellState.WHITE
                }
                BoardSize.SIX -> {
                    // For 6x6, place pieces in the center
                    board[2][2] = CellState.WHITE
                    board[2][3] = CellState.BLACK
                    board[3][2] = CellState.BLACK
                    board[3][3] = CellState.WHITE
                }
                BoardSize.EIGHT -> {
                    // For 8x8, standard Othello starting position
                    board[3][3] = CellState.WHITE
                    board[3][4] = CellState.BLACK
                    board[4][3] = CellState.BLACK
                    board[4][4] = CellState.WHITE
                }
            }

            return OthelloGame(
                board = board,
                boardSize = boardSize,
                currentPlayer = Player.BLACK,
                gameState = GameState.ONGOING,
                blackScore = 2,
                whiteScore = 2
            )
        }
    }

    fun copy(): OthelloGame {
        val size = boardSize.size
        return OthelloGame(
            board = Array(size) { row -> Array(size) { col -> board[row][col] } },
            boardSize = boardSize,
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
        if (boardSize != other.boardSize) return false
        if (currentPlayer != other.currentPlayer) return false
        if (gameState != other.gameState) return false
        if (blackScore != other.blackScore) return false
        if (whiteScore != other.whiteScore) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.contentDeepHashCode()
        result = 31 * result + boardSize.hashCode()
        result = 31 * result + currentPlayer.hashCode()
        result = 31 * result + gameState.hashCode()
        result = 31 * result + blackScore
        result = 31 * result + whiteScore
        return result
    }
}