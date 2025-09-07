package edu.ucne.registrojugadoresmv.presentation.jugador

import edu.ucne.registrojugadoresmv.domain.model.Jugador

data class JugadorUiState(
    val jugadorId: Int = 0,
    val nombres: String = "",
    val partidas: String = "",
    val nombresError: String? = null,
    val partidasError: String? = null,
    val jugadores: List<Jugador> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val success: Boolean = false)
    val selectedJugadorId: Int? = null