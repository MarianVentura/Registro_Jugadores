package edu.ucne.registrojugadoresmv.presentation.game

sealed class TicTacToeEvent {
    data class PartidaIdChanged(val partidaId: String) : TicTacToeEvent()
    object LoadMovimientos : TicTacToeEvent()
    data class MakeMove(val index: Int) : TicTacToeEvent()
    object ResetGame : TicTacToeEvent()
    object ClearMessages : TicTacToeEvent()
}