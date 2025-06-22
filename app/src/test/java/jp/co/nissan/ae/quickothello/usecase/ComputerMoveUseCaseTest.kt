package jp.co.nissan.ae.quickothello.usecase

import com.google.common.truth.Truth.assertThat
import jp.co.nissan.ae.quickothello.model.BoardSize
import jp.co.nissan.ae.quickothello.model.OthelloGame
import jp.co.nissan.ae.quickothello.model.Player
import jp.co.nissan.ae.quickothello.model.Position
import jp.co.nissan.ae.quickothello.repository.GameRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class ComputerMoveUseCaseTest {

    private lateinit var gameRepository: GameRepository
    private lateinit var computerMoveUseCase: ComputerMoveUseCase
    private lateinit var game: OthelloGame

    @Before
    fun setup() {
        gameRepository = mock()
        computerMoveUseCase = ComputerMoveUseCase(gameRepository)
        game = OthelloGame.createInitialGame(BoardSize.EIGHT)
            .copy(currentPlayer = Player.WHITE)
    }

    @Test
    fun `execute returns new game when computer finds valid move`() = runTest {
        val position = Position(2, 3)
        val newGame = game.copy(currentPlayer = Player.BLACK)

        whenever(gameRepository.calculateComputerMove(game)).thenReturn(position)
        whenever(gameRepository.makeMove(game, 2, 3)).thenReturn(newGame)

        val result = computerMoveUseCase.execute(game)

        assertThat(result).isEqualTo(newGame)
    }

    @Test
    fun `execute returns null when no valid move found`() = runTest {
        whenever(gameRepository.calculateComputerMove(game)).thenReturn(null)

        val result = computerMoveUseCase.execute(game)

        assertThat(result).isNull()
    }
}