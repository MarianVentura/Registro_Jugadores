package edu.ucne.registrojugadoresmv.presentation.partida.partidaUiState

import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.model.Jugador

data class PartidaUiState(
    val partidas: List<Partida> = emptyList(),
    val jugadores: List<Jugador> = emptyList(),
    val jugador1Id: Int? = null,
    val jugador2Id: Int? = null,
    val ganadorId: Int? = null,
    val esFinalizada: Boolean = false,
    val selectedPartidaId: Int? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val jugador1Error: String? = null,
    val jugador2Error: String? = null
)