package jp.co.nissan.ae.quickothello.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BoardSizeTest {

    @Test
    fun `size returns correct values`() {
        assertThat(BoardSize.FOUR.size).isEqualTo(4)
        assertThat(BoardSize.SIX.size).isEqualTo(6)
        assertThat(BoardSize.EIGHT.size).isEqualTo(8)
    }

    @Test
    fun `displayName returns correct strings`() {
        assertThat(BoardSize.FOUR.displayName()).isEqualTo("4x4")
        assertThat(BoardSize.SIX.displayName()).isEqualTo("6x6")
        assertThat(BoardSize.EIGHT.displayName()).isEqualTo("8x8")
    }
}