package jp.co.nissan.ae.quickothello.usecase

import com.google.common.truth.Truth.assertThat
import jp.co.nissan.ae.quickothello.model.BoardSize
import jp.co.nissan.ae.quickothello.model.GameMode
import jp.co.nissan.ae.quickothello.model.GameState
import jp.co.nissan.ae.quickothello.model.OthelloGame
import jp.co.nissan.ae.quickothello.model.Player
import jp.co.nissan.ae.quickothello.repository.GameRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class MakeMoveUseCaseTest {

    private lateinit var gameRepository: GameRepository
    private lateinit var makeMoveUseCase: MakeMoveUseCase
    private lateinit var game: OthelloGame

    @Before
    fun setup() {
        gameRepository = mock()
        makeMoveUseCase = MakeMoveUseCase(gameRepository)
        game = OthelloGame.createInitialGame(BoardSize.EIGHT)
    }

    @Test
    fun `execute returns Success for valid human move`() {
        val newGame = game.copy(currentPlayer = Player.WHITE)
        whenever(gameRepository.makeMove(game, 2, 3)).thenReturn(newGame)

        val result = makeMoveUseCase.execute(game, 2, 3, GameMode.HUMAN_VS_HUMAN)

        assertThat(result).isInstanceOf(MakeMoveUseCase.Result.Success::class.java)
        val success = result as MakeMoveUseCase.Result.Success
        assertThat(success.newGame).isEqualTo(newGame)
        assertThat(success.shouldComputerPlay).isFalse()
    }

    @Test
    fun `execute returns Success with shouldComputerPlay true for human vs computer`() {
        val newGame = game.copy(currentPlayer = Player.WHITE)
        whenever(gameRepository.makeMove(game, 2, 3)).thenReturn(newGame)

        val result = makeMoveUseCase.execute(game, 2, 3, GameMode.HUMAN_VS_COMPUTER)

        assertThat(result).isInstanceOf(MakeMoveUseCase.Result.Success::class.java)
        val success = result as MakeMoveUseCase.Result.Success
        assertThat(success.shouldComputerPlay).isTrue()
    }

    @Test
    fun `execute returns InvalidMove when repository returns null`() {
        whenever(gameRepository.makeMove(game, 0, 0)).thenReturn(null)

        val result = makeMoveUseCase.execute(game, 0, 0, GameMode.HUMAN_VS_HUMAN)

        assertThat(result).isEqualTo(MakeMoveUseCase.Result.InvalidMove)
    }

    @Test
    fun `execute returns GameNotOngoing when game is over`() {
        val gameOver = game.copy(gameState = GameState.BLACK_WINS)

        val result = makeMoveUseCase.execute(gameOver, 2, 3, GameMode.HUMAN_VS_HUMAN)

        assertThat(result).isEqualTo(MakeMoveUseCase.Result.GameNotOngoing)
    }

    @Test
    fun `execute returns NotPlayerTurn when it's computer's turn`() {
        val computerTurn = game.copy(currentPlayer = Player.WHITE)

        val result = makeMoveUseCase.execute(computerTurn, 2, 3, GameMode.HUMAN_VS_COMPUTER)

        assertThat(result).isEqualTo(MakeMoveUseCase.Result.NotPlayerTurn)
    }
}