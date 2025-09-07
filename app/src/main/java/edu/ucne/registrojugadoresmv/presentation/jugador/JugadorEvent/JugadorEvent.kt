// presentation/jugador/JugadorEvent.kt
package edu.ucne.registrojugadoresmv.presentation.jugador

sealed class JugadorEvent {
    data class NombresChanged(val nombres: String) : JugadorEvent()
    data class PartidasChanged(val partidas: String) : JugadorEvent()
    object SaveJugador : JugadorEvent()
    object ClearForm : JugadorEvent()
    data class DeleteJugador(val jugadorId: Int) : JugadorEvent()
    data class SelectJugador(val jugadorId: Int) : JugadorEvent()
}