package jp.co.nissan.ae.quickothello.model

enum class BoardSize(val size: Int) {
    FOUR(4),
    SIX(6),
    EIGHT(8);

    fun displayName(): String = when(this) {
        FOUR -> "4x4"
        SIX -> "6x6"
        EIGHT -> "8x8"
    }
}