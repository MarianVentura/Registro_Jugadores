package edu.ucne.registrojugadoresmv.presentation.partida

import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.model.PartidaConNombres

data class PartidaUiState(
    val partidaId: Int = 0,
    val fecha: String = "",
    val jugador1Id: Int = 0,
    val jugador2Id: Int = 0,
    val ganadorId: Int? = null,
    val esFinalizada: Boolean = false,

    // Errores de validación
    val fechaError: String? = null,
    val jugadoresError: String? = null,
    val ganadorError: String? = null,

    // Listas y estados
    val partidas: List<PartidaConNombres> = emptyList(),
    val jugadores: List<Jugador> = emptyList(),
    val selectedPartida: Partida? = null,
    val selectedPartidaId: Int? = null,

    // Estados de UI
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val showDeleteDialog: Boolean = false,
    val isEditing: Boolean = false,

    // Estados de dropdowns
    val showJugador1Dropdown: Boolean = false,
    val showJugador2Dropdown: Boolean = false,
    val showGanadorDropdown: Boolean = false
)