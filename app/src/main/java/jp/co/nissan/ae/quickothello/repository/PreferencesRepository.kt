package jp.co.nissan.ae.quickothello.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import jp.co.nissan.ae.quickothello.model.BoardSize
import jp.co.nissan.ae.quickothello.model.GameMode
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class PreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "othello_preferences"
        private const val KEY_BOARD_SIZE = "board_size"
        private const val KEY_GAME_MODE = "game_mode"
    }

    fun saveBoardSize(boardSize: BoardSize) {
        prefs.edit { putString(KEY_BOARD_SIZE, boardSize.name) }
    }

    fun getSavedBoardSize(): BoardSize {
        val savedValue = prefs.getString(KEY_BOARD_SIZE, BoardSize.EIGHT.name)
        return try {
            BoardSize.valueOf(savedValue!!)
        } catch (e: IllegalArgumentException) {
            BoardSize.EIGHT
        }
    }

    fun saveGameMode(gameMode: GameMode) {
        prefs.edit { putString(KEY_GAME_MODE, gameMode.name) }
    }

    fun getSavedGameMode(): GameMode {
        val savedValue = prefs.getString(KEY_GAME_MODE, GameMode.HUMAN_VS_HUMAN.name)
        return try {
            GameMode.valueOf(savedValue!!)
        } catch (e: IllegalArgumentException) {
            GameMode.HUMAN_VS_HUMAN
        }
    }
}