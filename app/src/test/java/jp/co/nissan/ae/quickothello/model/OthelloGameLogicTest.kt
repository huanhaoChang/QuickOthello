package jp.co.nissan.ae.quickothello.model

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class OthelloGameLogicTest {

    private lateinit var gameLogic: OthelloGameLogic
    private lateinit var game: OthelloGame

    @Before
    fun setup() {
        gameLogic = OthelloGameLogic()
        game = OthelloGame.createInitialGame(BoardSize.EIGHT)
    }

    @Test
    fun `isValidMove returns true for valid opening moves`() {
        // Black's valid opening moves
        assertThat(gameLogic.isValidMove(game, 2, 3, Player.BLACK)).isTrue()
        assertThat(gameLogic.isValidMove(game, 3, 2, Player.BLACK)).isTrue()
        assertThat(gameLogic.isValidMove(game, 4, 5, Player.BLACK)).isTrue()
        assertThat(gameLogic.isValidMove(game, 5, 4, Player.BLACK)).isTrue()
    }

    @Test
    fun `isValidMove returns false for invalid moves`() {
        // Empty corner - no pieces to flip
        assertThat(gameLogic.isValidMove(game, 0, 0, Player.BLACK)).isFalse()

        // Occupied cell
        assertThat(gameLogic.isValidMove(game, 3, 3, Player.BLACK)).isFalse()

        // Out of bounds
        assertThat(gameLogic.isValidMove(game, -1, 0, Player.BLACK)).isFalse()
        assertThat(gameLogic.isValidMove(game, 8, 8, Player.BLACK)).isFalse()
    }

    @Test
    fun `makeMove correctly flips pieces`() {
        // Black plays at (2,3)
        val newGame = gameLogic.makeMove(game, 2, 3)

        assertThat(newGame).isNotNull()
        newGame!!

        // Check the move was made
        assertThat(newGame.board[2][3]).isEqualTo(CellState.BLACK)

        // Check piece was flipped
        assertThat(newGame.board[3][3]).isEqualTo(CellState.BLACK)

        // Check turn changed
        assertThat(newGame.currentPlayer).isEqualTo(Player.WHITE)

        // Check scores updated
        assertThat(newGame.blackScore).isEqualTo(4)
        assertThat(newGame.whiteScore).isEqualTo(1)
    }

    @Test
    fun `makeMove returns null for invalid move`() {
        val newGame = gameLogic.makeMove(game, 0, 0)
        assertThat(newGame).isNull()
    }

    @Test
    fun `getValidMoves returns correct moves for initial position`() {
        val validMoves = gameLogic.getValidMoves(game, Player.BLACK)

        assertThat(validMoves).hasSize(4)
        assertThat(validMoves).containsExactly(
            Position(2, 3),
            Position(3, 2),
            Position(4, 5),
            Position(5, 4)
        )
    }

    @Test
    fun `game correctly handles no valid moves for one player`() {
        // Create a scenario where one player has no moves
        val customGame = OthelloGame.createInitialGame(BoardSize.FOUR)

        // Fill most of the board with black
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                if (row != 1 || col != 1) {
                    customGame.board[row][col] = CellState.BLACK
                }
            }
        }
        customGame.board[1][1] = CellState.WHITE

        val testGame = customGame.copy(currentPlayer = Player.WHITE)
        val validMoves = gameLogic.getValidMoves(testGame, Player.WHITE)

        assertThat(validMoves).isEmpty()
    }

    @Test
    fun `game ends when neither player can move`() {
        // Create an endgame scenario
        val endGame = OthelloGame.createInitialGame(BoardSize.FOUR)

        // Fill entire board
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                endGame.board[row][col] = if (row < 2) CellState.BLACK else CellState.WHITE
            }
        }

        val testGame = endGame.copy(
            currentPlayer = Player.BLACK,
            blackScore = 8,
            whiteScore = 8
        )

        // Try to make a move on a full board
        val result = gameLogic.makeMove(testGame, 0, 0)

        assertThat(result).isNull()
    }
}