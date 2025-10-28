package edu.ucne.registrojugadoresmv.presentation.game

import edu.ucne.registrojugadoresmv.domain.model.Movimiento

data class TicTacToeUiState(
    val board: List<String> = List(9) { "" },
    val currentPlayer: String = "X",
    val winner: String? = null,
    val gameOver: Boolean = false,
    val partidaIdText: String = "",
    val currentPartidaId: Int? = null,
    val movimientos: List<Movimiento> = emptyList(),
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)