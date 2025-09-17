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
    val selectedJugador: Jugador? = null,
    val showDeleteDialog: Boolean = false,
    val isEditing: Boolean = false
)

// Extensiones útiles para el estado
val JugadorUiState.isSuccess: Boolean
    get() = successMessage != null

val JugadorUiState.hasError: Boolean
    get() = errorMessage != null || nombresError != null || partidasError != null

val JugadorUiState.canSave: Boolean
    get() = nombres.isNotBlank() && partidas.isNotBlank() && !isLoading