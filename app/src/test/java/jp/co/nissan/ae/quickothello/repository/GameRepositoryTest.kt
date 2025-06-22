package jp.co.nissan.ae.quickothello.repository

import com.google.common.truth.Truth.assertThat
import jp.co.nissan.ae.quickothello.model.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class GameRepositoryTest {

    private lateinit var gameLogic: GameLogic
    private lateinit var computerPlayer: ComputerPlayer
    private lateinit var gameRepository: GameRepository

    @Before
    fun setup() {
        gameLogic = mock()
        computerPlayer = mock()
        gameRepository = GameRepository(gameLogic, computerPlayer)
    }

    @Test
    fun `createNewGame creates initial game with correct board size`() {
        val game = gameRepository.createNewGame(BoardSize.SIX)

        assertThat(game.boardSize).isEqualTo(BoardSize.SIX)
        assertThat(game.currentPlayer).isEqualTo(Player.BLACK)
        assertThat(game.gameState).isEqualTo(GameState.ONGOING)
    }

    @Test
    fun `makeMove delegates to game logic`() {
        val game = OthelloGame.createInitialGame()
        val newGame = game.copy()
        whenever(gameLogic.makeMove(game, 2, 3)).thenReturn(newGame)

        val result = gameRepository.makeMove(game, 2, 3)

        assertThat(result).isEqualTo(newGame)
        verify(gameLogic).makeMove(game, 2, 3)
    }

    @Test
    fun `getValidMoves delegates to game logic`() {
        val game = OthelloGame.createInitialGame()
        val moves = listOf(Position(2, 3), Position(3, 2))
        whenever(gameLogic.getValidMoves(game, Player.BLACK)).thenReturn(moves)

        val result = gameRepository.getValidMoves(game, Player.BLACK)

        assertThat(result).isEqualTo(moves)
        verify(gameLogic).getValidMoves(game, Player.BLACK)
    }

    @Test
    fun `isValidMove delegates to game logic`() {
        val game = OthelloGame.createInitialGame()
        whenever(gameLogic.isValidMove(game, 2, 3, Player.BLACK)).thenReturn(true)

        val result = gameRepository.isValidMove(game, 2, 3, Player.BLACK)

        assertThat(result).isTrue()
        verify(gameLogic).isValidMove(game, 2, 3, Player.BLACK)
    }

    @Test
    fun `calculateComputerMove delegates to computer player`() = runTest {
        val game = OthelloGame.createInitialGame()
        val position = Position(2, 3)
        whenever(computerPlayer.calculateBestMove(game)).thenReturn(position)

        val result = gameRepository.calculateComputerMove(game)

        assertThat(result).isEqualTo(position)
        verify(computerPlayer).calculateBestMove(game)
    }
}