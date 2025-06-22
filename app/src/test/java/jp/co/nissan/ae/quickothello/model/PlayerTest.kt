package jp.co.nissan.ae.quickothello.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PlayerTest {

    @Test
    fun `toCellState returns correct cell state`() {
        assertThat(Player.BLACK.toCellState()).isEqualTo(CellState.BLACK)
        assertThat(Player.WHITE.toCellState()).isEqualTo(CellState.WHITE)
    }

    @Test
    fun `opposite returns opposite player`() {
        assertThat(Player.BLACK.opposite()).isEqualTo(Player.WHITE)
        assertThat(Player.WHITE.opposite()).isEqualTo(Player.BLACK)
    }
}