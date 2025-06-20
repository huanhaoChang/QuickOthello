package jp.co.nissan.ae.quickothello.model

enum class Player {
    BLACK, WHITE;

    fun toCellState(): CellState = when (this) {
        BLACK -> CellState.BLACK
        WHITE -> CellState.WHITE
    }

    fun opposite(): Player = when (this) {
        BLACK -> WHITE
        WHITE -> BLACK
    }
}