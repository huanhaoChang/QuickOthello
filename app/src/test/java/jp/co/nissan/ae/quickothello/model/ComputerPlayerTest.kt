package jp.co.nissan.ae.quickothello.model

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ComputerPlayerTest {

    private lateinit var gameLogic: OthelloGameLogic
    private lateinit var computerPlayer: ComputerPlayer

    @Before
    fun setup() {
        gameLogic = OthelloGameLogic()
        computerPlayer = ComputerPlayer(gameLogic)
    }

    @Test
    fun `calculateBestMove returns null when not computer turn`() = runTest {
        val game = OthelloGame.createInitialGame(BoardSize.EIGHT)

        val move = computerPlayer.calculateBestMove(game)

        assertThat(move).isNull()
    }

    @Test
    fun `calculateBestMove returns valid move for computer turn`() = runTest {
        val game = OthelloGame.createInitialGame(BoardSize.EIGHT)
            .copy(currentPlayer = Player.WHITE)

        val move = computerPlayer.calculateBestMove(game)

        assertThat(move).isNotNull()

        // Verify it's a valid move
        val validMoves = gameLogic.getValidMoves(game, Player.WHITE)
        assertThat(validMoves).contains(move)
    }

    @Test
    fun `calculateBestMove returns null when no valid moves`() = runTest {
        // Create a game where white has no valid moves
        val game = OthelloGame.createInitialGame(BoardSize.FOUR)

        // Fill board leaving no valid moves for white
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                game.board[row][col] = CellState.BLACK
            }
        }
        game.board[0][0] = CellState.WHITE

        val testGame = game.copy(
            currentPlayer = Player.WHITE,
            blackScore = 15,
            whiteScore = 1
        )

        val move = computerPlayer.calculateBestMove(testGame)

        assertThat(move).isNull()
    }

    @Test
    fun `calculateBestMove prefers corners`() = runTest {
        // Create a scenario where corner move is available
        val game = OthelloGame.createInitialGame(BoardSize.EIGHT)

        // Set up board so white can take corner (0,0)
        game.board[0][0] = CellState.EMPTY
        game.board[0][1] = CellState.BLACK
        game.board[0][2] = CellState.BLACK
        game.board[0][3] = CellState.WHITE

        game.board[1][0] = CellState.BLACK
        game.board[1][1] = CellState.BLACK

        game.board[2][0] = CellState.BLACK
        game.board[3][0] = CellState.WHITE

        val testGame = game.copy(currentPlayer = Player.WHITE)

        val move = computerPlayer.calculateBestMove(testGame)

        // Computer should choose the corner
        assertThat(move).isEqualTo(Position(0, 0))
    }
}