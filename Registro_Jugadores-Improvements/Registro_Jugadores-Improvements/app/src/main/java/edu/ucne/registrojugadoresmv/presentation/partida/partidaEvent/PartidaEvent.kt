package edu.ucne.registrojugadoresmv.presentation.partida.partidaEvent

import edu.ucne.registrojugadoresmv.domain.model.Partida

sealed class PartidaEvent {
    data class Jugador1Changed(val jugadorId: Int) : PartidaEvent()
    data class Jugador2Changed(val jugadorId: Int) : PartidaEvent()
    data class GanadorChanged(val ganadorId: Int?) : PartidaEvent()
    data class EsFinalizadaChanged(val esFinalizada: Boolean) : PartidaEvent()
    object SavePartida : PartidaEvent()
    object ClearForm : PartidaEvent()
    data class DeletePartida(val partidaId: Int) : PartidaEvent()
    data class EditPartida(val partida: Partida) : PartidaEvent()
    data class SelectPartida(val partidaId: Int) : PartidaEvent()
    data class ConfirmDeletePartida(val partida: Partida) : PartidaEvent()
}