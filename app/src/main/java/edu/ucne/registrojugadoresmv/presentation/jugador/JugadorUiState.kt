package edu.ucne.registrojugadoresmv.presentation.jugador

import edu.ucne.registrojugadoresmv.domain.model.Jugador

data class JugadorUiState(
    val nombres: String = "",
    val email: String = "",
    val partidas: String = "",
    val nombresError: String? = null,
    val emailError: String? = null,
    val partidasError: String? = null,
    val jugadores: List<Jugador> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val selectedJugador: Jugador? = null,
    val showDeleteDialog: Boolean = false,
    val isEditing: Boolean = false
)