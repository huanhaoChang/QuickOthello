package jp.co.nissan.ae.quickothello.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ComputerPlayer(private val gameLogic: OthelloGameLogic) {

    companion object {
        private const val MAX_DEPTH = 5
        private const val CORNER_WEIGHT = 100
        private const val EDGE_WEIGHT = 10
        private const val MOBILITY_WEIGHT = 5
    }

    suspend fun calculateBestMove(game: OthelloGame): Position? = withContext(Dispatchers.Default) {
        if (game.currentPlayer != Player.WHITE || game.gameState != GameState.ONGOING) {
            return@withContext null
        }

        val validMoves = gameLogic.getValidMoves(game, Player.WHITE)
        if (validMoves.isEmpty()) return@withContext null

        var bestMove = validMoves.first()
        var bestScore = Int.MIN_VALUE

        for (move in validMoves) {
            val newGame = gameLogic.makeMove(game, move.row, move.col)
            if (newGame != null) {
                val score = minimax(newGame, MAX_DEPTH - 1, Int.MIN_VALUE, Int.MAX_VALUE, false)
                if (score > bestScore) {
                    bestScore = score
                    bestMove = move
                }
            }
        }

        return@withContext bestMove
    }

    private fun minimax(
        game: OthelloGame,
        depth: Int,
        alpha: Int,
        beta: Int,
        isMaximizingPlayer: Boolean
    ): Int {
        // Base cases
        if (depth == 0 || game.gameState != GameState.ONGOING) {
            return evaluateBoard(game)
        }

        val currentPlayer = game.currentPlayer
        val validMoves = gameLogic.getValidMoves(game, currentPlayer)

        // No valid moves - pass turn
        if (validMoves.isEmpty()) {
            val nextPlayer = currentPlayer.opposite()
            val nextValidMoves = gameLogic.getValidMoves(game, nextPlayer)

            // Game over if both players have no moves
            if (nextValidMoves.isEmpty()) {
                return evaluateBoard(game)
            }

            // Pass turn and continue
            val passedGame = game.copy(currentPlayer = nextPlayer)
            return minimax(passedGame, depth - 1, alpha, beta, !isMaximizingPlayer)
        }

        var alphaLocal = alpha
        var betaLocal = beta

        if (isMaximizingPlayer) {
            var maxEval = Int.MIN_VALUE

            for (move in validMoves) {
                val newGame = gameLogic.makeMove(game, move.row, move.col)
                if (newGame != null) {
                    val eval = minimax(newGame, depth - 1, alphaLocal, betaLocal, false)
                    maxEval = maxOf(maxEval, eval)
                    alphaLocal = maxOf(alphaLocal, eval)

                    if (betaLocal <= alphaLocal) {
                        break // Beta cutoff
                    }
                }
            }

            return maxEval
        } else {
            var minEval = Int.MAX_VALUE

            for (move in validMoves) {
                val newGame = gameLogic.makeMove(game, move.row, move.col)
                if (newGame != null) {
                    val eval = minimax(newGame, depth - 1, alphaLocal, betaLocal, true)
                    minEval = minOf(minEval, eval)
                    betaLocal = minOf(betaLocal, eval)

                    if (betaLocal <= alphaLocal) {
                        break // Alpha cutoff
                    }
                }
            }

            return minEval
        }
    }

    private fun evaluateBoard(game: OthelloGame): Int {
        // If game is over, return a definitive score
        when (game.gameState) {
            GameState.WHITE_WINS -> return 10000
            GameState.BLACK_WINS -> return -10000
            GameState.DRAW -> return 0
            else -> {}
        }

        var score = 0
        val size = game.boardSize.size

        // Disk count difference
        val diskDifference = game.whiteScore - game.blackScore
        score += diskDifference * 10

        // Position weights
        for (row in 0 until size) {
            for (col in 0 until size) {
                val cellValue = when (game.board[row][col]) {
                    CellState.WHITE -> 1
                    CellState.BLACK -> -1
                    CellState.EMPTY -> 0
                }

                if (cellValue != 0) {
                    // Corner positions are most valuable
                    if (isCorner(row, col, size)) {
                        score += cellValue * CORNER_WEIGHT
                    }
                    // Edge positions are valuable
                    else if (isEdge(row, col, size)) {
                        score += cellValue * EDGE_WEIGHT
                    }
                    // Regular positions
                    else {
                        score += cellValue
                    }
                }
            }
        }

        // Mobility (number of valid moves)
        val whiteMobility = gameLogic.getValidMoves(game, Player.WHITE).size
        val blackMobility = gameLogic.getValidMoves(game, Player.BLACK).size
        score += (whiteMobility - blackMobility) * MOBILITY_WEIGHT

        return score
    }

    private fun isCorner(row: Int, col: Int, size: Int): Boolean {
        return (row == 0 || row == size - 1) && (col == 0 || col == size - 1)
    }

    private fun isEdge(row: Int, col: Int, size: Int): Boolean {
        return row == 0 || row == size - 1 || col == 0 || col == size - 1
    }
}