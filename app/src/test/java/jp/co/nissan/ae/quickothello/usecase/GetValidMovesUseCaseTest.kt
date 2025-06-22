package jp.co.nissan.ae.quickothello.usecase

import com.google.common.truth.Truth.assertThat
import jp.co.nissan.ae.quickothello.model.BoardSize
import jp.co.nissan.ae.quickothello.model.OthelloGame
import jp.co.nissan.ae.quickothello.model.Player
import jp.co.nissan.ae.quickothello.model.Position
import jp.co.nissan.ae.quickothello.repository.GameRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class GetValidMovesUseCaseTest {

    private lateinit var gameRepository: GameRepository
    private lateinit var getValidMovesUseCase: GetValidMovesUseCase
    private lateinit var game: OthelloGame

    @Before
    fun setup() {
        gameRepository = mock()
        getValidMovesUseCase = GetValidMovesUseCase(gameRepository)
        game = OthelloGame.createInitialGame(BoardSize.EIGHT)
    }

    @Test
    fun `execute returns valid moves from repository`() {
        val expectedMoves = listOf(
            Position(2, 3),
            Position(3, 2),
            Position(4, 5),
            Position(5, 4)
        )
        whenever(gameRepository.getValidMoves(game, Player.BLACK)).thenReturn(expectedMoves)

        val result = getValidMovesUseCase.execute(game)

        assertThat(result).isEqualTo(expectedMoves)
        verify(gameRepository).getValidMoves(game, Player.BLACK)
    }

    @Test
    fun `execute returns empty list when no valid moves`() {
        whenever(gameRepository.getValidMoves(game, Player.BLACK)).thenReturn(emptyList())

        val result = getValidMovesUseCase.execute(game)

        assertThat(result).isEmpty()
    }
}