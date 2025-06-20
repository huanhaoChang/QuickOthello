package jp.co.nissan.ae.quickothello.model

data class OthelloGame(
    val board: Array<Array<CellState>> = Array(8) { Array(8) { CellState.EMPTY } },
    val currentPlayer: Player = Player.BLACK,
    val gameState: GameState = GameState.ONGOING,
    val blackScore: Int = 2,
    val whiteScore: Int = 2
) {
    init {
        // Set initial board position
        board[3][3] = CellState.WHITE
        board[3][4] = CellState.BLACK
        board[4][3] = CellState.BLACK
        board[4][4] = CellState.WHITE
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

enum class CellState {
    EMPTY, BLACK, WHITE
}

enum class Player {
    BLACK, WHITE;

    fun toCellState(): CellState = when (this) {
        BLACK -> CellState.BLACK
        WHITE -> CellState.WHITE
    }

    fun opposite(): Player = when (this) {
        BLACK -> WHITE
        WHITE -> BLACK
    }
}

enum class GameState {
    ONGOING, BLACK_WINS, WHITE_WINS, DRAW
}

data class Position(val row: Int, val col: Int)

class OthelloGameLogic {
    private val directions = listOf(
        Position(-1, -1), Position(-1, 0), Position(-1, 1),
        Position(0, -1),                    Position(0, 1),
        Position(1, -1),  Position(1, 0),  Position(1, 1)
    )

    fun makeMove(game: OthelloGame, row: Int, col: Int): OthelloGame? {
        if (!isValidMove(game, row, col, game.currentPlayer)) {
            return null
        }

        val newGame = game.copy()
        val cellsToFlip = getCellsToFlip(game, row, col, game.currentPlayer)

        // Place the disk
        newGame.board[row][col] = game.currentPlayer.toCellState()

        // Flip the disks
        cellsToFlip.forEach { pos ->
            newGame.board[pos.row][pos.col] = game.currentPlayer.toCellState()
        }

        // Update scores
        val (blackCount, whiteCount) = countDisks(newGame.board)
        val nextPlayer = game.currentPlayer.opposite()

        // Check if next player has valid moves
        val hasValidMoves = hasAnyValidMove(newGame, nextPlayer)

        return if (hasValidMoves) {
            newGame.copy(
                currentPlayer = nextPlayer,
                blackScore = blackCount,
                whiteScore = whiteCount
            )
        } else {
            // Check if current player still has moves
            val currentPlayerHasMoves = hasAnyValidMove(newGame, game.currentPlayer)
            if (currentPlayerHasMoves) {
                // Skip opponent's turn
                newGame.copy(
                    currentPlayer = game.currentPlayer,
                    blackScore = blackCount,
                    whiteScore = whiteCount
                )
            } else {
                // Game over
                val gameState = when {
                    blackCount > whiteCount -> GameState.BLACK_WINS
                    whiteCount > blackCount -> GameState.WHITE_WINS
                    else -> GameState.DRAW
                }
                newGame.copy(
                    gameState = gameState,
                    blackScore = blackCount,
                    whiteScore = whiteCount
                )
            }
        }
    }

    fun isValidMove(game: OthelloGame, row: Int, col: Int, player: Player): Boolean {
        if (row !in 0..7 || col !in 0..7) return false
        if (game.board[row][col] != CellState.EMPTY) return false
        if (game.gameState != GameState.ONGOING) return false

        return getCellsToFlip(game, row, col, player).isNotEmpty()
    }

    fun getValidMoves(game: OthelloGame, player: Player): List<Position> {
        val validMoves = mutableListOf<Position>()
        for (row in 0..7) {
            for (col in 0..7) {
                if (isValidMove(game, row, col, player)) {
                    validMoves.add(Position(row, col))
                }
            }
        }
        return validMoves
    }

    private fun getCellsToFlip(game: OthelloGame, row: Int, col: Int, player: Player): List<Position> {
        val cellsToFlip = mutableListOf<Position>()
        val playerCell = player.toCellState()
        val opponentCell = player.opposite().toCellState()

        for (direction in directions) {
            val potentialFlips = mutableListOf<Position>()
            var currentRow = row + direction.row
            var currentCol = col + direction.col

            // Find opponent's disks in this direction
            while (currentRow in 0..7 && currentCol in 0..7 &&
                game.board[currentRow][currentCol] == opponentCell) {
                potentialFlips.add(Position(currentRow, currentCol))
                currentRow += direction.row
                currentCol += direction.col
            }

            // Check if we found player's disk at the end
            if (currentRow in 0..7 && currentCol in 0..7 &&
                game.board[currentRow][currentCol] == playerCell &&
                potentialFlips.isNotEmpty()) {
                cellsToFlip.addAll(potentialFlips)
            }
        }

        return cellsToFlip
    }

    private fun hasAnyValidMove(game: OthelloGame, player: Player): Boolean {
        for (row in 0..7) {
            for (col in 0..7) {
                if (isValidMove(game, row, col, player)) {
                    return true
                }
            }
        }
        return false
    }

    private fun countDisks(board: Array<Array<CellState>>): Pair<Int, Int> {
        var blackCount = 0
        var whiteCount = 0

        for (row in board) {
            for (cell in row) {
                when (cell) {
                    CellState.BLACK -> blackCount++
                    CellState.WHITE -> whiteCount++
                    else -> {}
                }
            }
        }

        return Pair(blackCount, whiteCount)
    }
}
