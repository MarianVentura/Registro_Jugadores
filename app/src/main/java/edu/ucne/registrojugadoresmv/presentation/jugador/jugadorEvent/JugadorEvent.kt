package edu.ucne.registrojugadoresmv.presentation.jugador.jugadorEvent

import edu.ucne.registrojugadoresmv.domain.model.Jugador
sealed class JugadorEvent {
    data class NombresChanged(val nombres: String) : JugadorEvent()
    data class PartidasChanged(val partidas: String) : JugadorEvent()
    object SaveJugador : JugadorEvent()
    object ClearForm : JugadorEvent()
    data class DeleteJugador(val jugadorId: Int) : JugadorEvent()
    data class SelectJugador(val jugadorId: Int) : JugadorEvent()
    data class EditJugador(val jugador: Jugador) : JugadorEvent()
    data class ConfirmDeleteJugador(val jugador: Jugador) : JugadorEvent()
}

