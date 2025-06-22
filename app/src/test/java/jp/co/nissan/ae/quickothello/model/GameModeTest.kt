package jp.co.nissan.ae.quickothello.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GameModeTest {

    @Test
    fun `displayName returns correct strings`() {
        assertThat(GameMode.HUMAN_VS_HUMAN.displayName()).isEqualTo("Play with Human")
        assertThat(GameMode.HUMAN_VS_COMPUTER.displayName()).isEqualTo("Play with Computer")
    }
}