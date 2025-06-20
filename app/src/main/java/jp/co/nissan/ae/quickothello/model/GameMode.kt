package jp.co.nissan.ae.quickothello.model

enum class GameMode {
    HUMAN_VS_HUMAN,
    HUMAN_VS_COMPUTER;

    fun displayName(): String = when(this) {
        HUMAN_VS_HUMAN -> "Play with Human"
        HUMAN_VS_COMPUTER -> "Play with Computer"
    }
}