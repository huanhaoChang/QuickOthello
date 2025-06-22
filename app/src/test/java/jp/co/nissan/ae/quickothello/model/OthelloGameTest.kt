package jp.co.nissan.ae.quickothello.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class OthelloGameTest {

    @Test
    fun `createInitialGame creates correct 8x8 board`() {
        val game = OthelloGame.createInitialGame(BoardSize.EIGHT)

        assertThat(game.boardSize).isEqualTo(BoardSize.EIGHT)
        assertThat(game.board.size).isEqualTo(8)
        assertThat(game.board[0].size).isEqualTo(8)
        assertThat(game.currentPlayer).isEqualTo(Player.BLACK)
        assertThat(game.gameState).isEqualTo(GameState.ONGOING)
        assertThat(game.blackScore).isEqualTo(2)
        assertThat(game.whiteScore).isEqualTo(2)

        // Check initial positions
        assertThat(game.board[3][3]).isEqualTo(CellState.WHITE)
        assertThat(game.board[3][4]).isEqualTo(CellState.BLACK)
        assertThat(game.board[4][3]).isEqualTo(CellState.BLACK)
        assertThat(game.board[4][4]).isEqualTo(CellState.WHITE)
    }

    @Test
    fun `createInitialGame creates correct 6x6 board`() {
        val game = OthelloGame.createInitialGame(BoardSize.SIX)

        assertThat(game.boardSize).isEqualTo(BoardSize.SIX)
        assertThat(game.board.size).isEqualTo(6)
        assertThat(game.board[0].size).isEqualTo(6)

        // Check initial positions for 6x6
        assertThat(game.board[2][2]).isEqualTo(CellState.WHITE)
        assertThat(game.board[2][3]).isEqualTo(CellState.BLACK)
        assertThat(game.board[3][2]).isEqualTo(CellState.BLACK)
        assertThat(game.board[3][3]).isEqualTo(CellState.WHITE)
    }

    @Test
    fun `createInitialGame creates correct 4x4 board`() {
        val game = OthelloGame.createInitialGame(BoardSize.FOUR)

        assertThat(game.boardSize).isEqualTo(BoardSize.FOUR)
        assertThat(game.board.size).isEqualTo(4)
        assertThat(game.board[0].size).isEqualTo(4)

        // Check initial positions for 4x4
        assertThat(game.board[1][1]).isEqualTo(CellState.WHITE)
        assertThat(game.board[1][2]).isEqualTo(CellState.BLACK)
        assertThat(game.board[2][1]).isEqualTo(CellState.BLACK)
        assertThat(game.board[2][2]).isEqualTo(CellState.WHITE)
    }

    @Test
    fun `copy creates deep copy of game`() {
        val original = OthelloGame.createInitialGame(BoardSize.EIGHT)
        val copy = original.copy()

        // Modify the copy
        copy.board[0][0] = CellState.BLACK

        // Original should be unchanged
        assertThat(original.board[0][0]).isEqualTo(CellState.EMPTY)
        assertThat(copy.board[0][0]).isEqualTo(CellState.BLACK)
    }

    @Test
    fun `equals returns true for identical games`() {
        val game1 = OthelloGame.createInitialGame(BoardSize.EIGHT)
        val game2 = OthelloGame.createInitialGame(BoardSize.EIGHT)

        assertThat(game1).isEqualTo(game2)
    }

    @Test
    fun `equals returns false for different games`() {
        val game1 = OthelloGame.createInitialGame(BoardSize.EIGHT)
        val game2 = OthelloGame.createInitialGame(BoardSize.SIX)

        assertThat(game1).isNotEqualTo(game2)
    }
}