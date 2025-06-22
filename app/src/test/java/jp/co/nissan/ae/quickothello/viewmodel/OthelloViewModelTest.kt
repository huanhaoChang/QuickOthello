package jp.co.nissan.ae.quickothello.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import jp.co.nissan.ae.quickothello.model.*
import jp.co.nissan.ae.quickothello.repository.GameRepository
import jp.co.nissan.ae.quickothello.repository.PreferencesRepository
import jp.co.nissan.ae.quickothello.usecase.ComputerMoveUseCase
import jp.co.nissan.ae.quickothello.usecase.GetValidMovesUseCase
import jp.co.nissan.ae.quickothello.usecase.MakeMoveUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class OthelloViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var gameRepository: GameRepository
    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var makeMoveUseCase: MakeMoveUseCase
    private lateinit var computerMoveUseCase: ComputerMoveUseCase
    private lateinit var getValidMovesUseCase: GetValidMovesUseCase
    private lateinit var viewModel: OthelloViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        gameRepository = mock()
        preferencesRepository = mock()
        makeMoveUseCase = mock()
        computerMoveUseCase = mock()
        getValidMovesUseCase = mock()

        // Setup default behavior
        whenever(preferencesRepository.getSavedBoardSize()).thenReturn(BoardSize.EIGHT)
        whenever(preferencesRepository.getSavedGameMode()).thenReturn(GameMode.HUMAN_VS_HUMAN)
        whenever(gameRepository.createNewGame(any())).thenReturn(OthelloGame.createInitialGame())
        whenever(getValidMovesUseCase.execute(any())).thenReturn(emptyList())

        viewModel = OthelloViewModel(
            gameRepository,
            preferencesRepository,
            makeMoveUseCase,
            computerMoveUseCase,
            getValidMovesUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loaded from preferences`() = runTest {
        whenever(preferencesRepository.getSavedBoardSize()).thenReturn(BoardSize.SIX)
        whenever(preferencesRepository.getSavedGameMode()).thenReturn(GameMode.HUMAN_VS_COMPUTER)

        val viewModel = OthelloViewModel(
            gameRepository,
            preferencesRepository,
            makeMoveUseCase,
            computerMoveUseCase,
            getValidMovesUseCase
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.selectedBoardSize).isEqualTo(BoardSize.SIX)
            assertThat(state.gameMode).isEqualTo(GameMode.HUMAN_VS_COMPUTER)
        }
    }

    @Test
    fun `onCellClick handles successful move`() = runTest {
        val game = OthelloGame.createInitialGame()
        val newGame = game.copy(currentPlayer = Player.WHITE)

        whenever(makeMoveUseCase.execute(any(), eq(2), eq(3), any()))
            .thenReturn(MakeMoveUseCase.Result.Success(newGame, false))
        whenever(getValidMovesUseCase.execute(newGame)).thenReturn(emptyList())

        viewModel.onCellClick(2, 3)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.game).isEqualTo(newGame)
            assertThat(state.showInvalidMoveMessage).isFalse()
        }
    }

    @Test
    fun `onCellClick shows invalid move message`() = runTest {
        whenever(makeMoveUseCase.execute(any(), eq(0), eq(0), any()))
            .thenReturn(MakeMoveUseCase.Result.InvalidMove)

        viewModel.onCellClick(0, 0)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showInvalidMoveMessage).isTrue()
        }
    }

    @Test
    fun `resetGame creates new game`() = runTest {
        val newGame = OthelloGame.createInitialGame(BoardSize.FOUR)
        whenever(gameRepository.createNewGame(BoardSize.FOUR)).thenReturn(newGame)

        viewModel.resetGame(BoardSize.FOUR, GameMode.HUMAN_VS_COMPUTER)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.game).isEqualTo(newGame)
            assertThat(state.selectedBoardSize).isEqualTo(BoardSize.FOUR)
            assertThat(state.gameMode).isEqualTo(GameMode.HUMAN_VS_COMPUTER)
            assertThat(state.hasGameInProgress).isTrue()
        }
    }

    @Test
    fun `updateBoardSize saves preference`() = runTest {
        viewModel.updateBoardSize(BoardSize.SIX)

        verify(preferencesRepository).saveBoardSize(BoardSize.SIX)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.selectedBoardSize).isEqualTo(BoardSize.SIX)
        }
    }

    @Test
    fun `updateGameMode saves preference`() = runTest {
        viewModel.updateGameMode(GameMode.HUMAN_VS_COMPUTER)

        verify(preferencesRepository).saveGameMode(GameMode.HUMAN_VS_COMPUTER)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.gameMode).isEqualTo(GameMode.HUMAN_VS_COMPUTER)
        }
    }

    @Test
    fun `dismissInvalidMoveMessage clears message`() = runTest {
        // First trigger invalid move
        whenever(makeMoveUseCase.execute(any(), eq(0), eq(0), any()))
            .thenReturn(MakeMoveUseCase.Result.InvalidMove)

        viewModel.onCellClick(0, 0)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then dismiss
        viewModel.dismissInvalidMoveMessage()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showInvalidMoveMessage).isFalse()
        }
    }

    @Test
    fun `computer makes move after human in vs computer mode`() = runTest {
        val game = OthelloGame.createInitialGame()
        val afterHumanMove = game.copy(currentPlayer = Player.WHITE)
        val afterComputerMove = afterHumanMove.copy(currentPlayer = Player.BLACK)

        whenever(makeMoveUseCase.execute(any(), eq(2), eq(3), eq(GameMode.HUMAN_VS_COMPUTER)))
            .thenReturn(MakeMoveUseCase.Result.Success(afterHumanMove, true))
        whenever(computerMoveUseCase.execute(afterHumanMove))
            .thenReturn(afterComputerMove)
        whenever(getValidMovesUseCase.execute(any())).thenReturn(emptyList())

        viewModel.updateGameMode(GameMode.HUMAN_VS_COMPUTER)
        viewModel.onCellClick(2, 3)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(computerMoveUseCase).execute(afterHumanMove)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.game).isEqualTo(afterComputerMove)
            assertThat(state.isComputerThinking).isFalse()
        }
    }

    @Test
    fun `onAppResumed shows dialog for game in progress`() = runTest {
        // Start a game and make a move
        val game = OthelloGame.createInitialGame()
        val gameInProgress = game.copy(blackScore = 3, whiteScore = 1)

        // Simulate app lifecycle
        viewModel.onAppPaused() // First pause

        // Update state to have game in progress
        viewModel.resetGame()
        testDispatcher.scheduler.advanceUntilIdle()

        // Manually set a progressed game state for testing
        val currentState = viewModel.uiState.value
        whenever(gameRepository.createNewGame(any())).thenReturn(gameInProgress)
        viewModel.resetGame()

        viewModel.onAppResumed()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showResumeDialog).isTrue()
        }
    }
}