package edu.ucne.registrojugadoresmv.presentation.partida

import edu.ucne.registrojugadoresmv.domain.model.Partida

sealed class PartidaEvent {
    data class FechaChanged(val fecha: String) : PartidaEvent()
    data class Jugador1Changed(val jugador1Id: Int) : PartidaEvent()
    data class Jugador2Changed(val jugador2Id: Int) : PartidaEvent()
    data class GanadorChanged(val ganadorId: Int?) : PartidaEvent()
    data class EsFinalizadaChanged(val esFinalizada: Boolean) : PartidaEvent()

    data object SavePartida : PartidaEvent()
    data object ClearForm : PartidaEvent()

    data class DeletePartida(val partidaId: Int) : PartidaEvent()
    data class SelectPartida(val partidaId: Int) : PartidaEvent()
    data class EditPartida(val partida: Partida) : PartidaEvent()
    data class ConfirmDeletePartida(val partida: Partida) : PartidaEvent()

    data object LoadJugadores : PartidaEvent()
    data object DismissDeleteDialog : PartidaEvent()
    data object ShowDeleteDialog : PartidaEvent()
}