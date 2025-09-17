package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class ValidatePartidaUseCase @Inject constructor(
    private val jugadorRepository: JugadorRepository
) {
    suspend fun validateJugadores(jugador1Id: Int, jugador2Id: Int): String? {
        return when {
            jugador1Id == 0 -> "Debe seleccionar el jugador 1"
            jugador2Id == 0 -> "Debe seleccionar el jugador 2"
            jugador1Id == jugador2Id -> "Un jugador no puede jugar contra sí mismo"
            else -> {
                // Verificar que ambos jugadores existan
                val jugador1 = jugadorRepository.getJugadorById(jugador1Id)
                val jugador2 = jugadorRepository.getJugadorById(jugador2Id)

                when {
                    jugador1 == null -> "El jugador 1 seleccionado no existe"
                    jugador2 == null -> "El jugador 2 seleccionado no existe"
                    else -> null
                }
            }
        }
    }

    fun validateFecha(fecha: String): String? {
        return when {
            fecha.isBlank() -> "La fecha es obligatoria"
            else -> null
        }
    }

    fun validateGanador(ganadorId: Int?, esFinalizada: Boolean, jugador1Id: Int, jugador2Id: Int): String? {
        return when {
            esFinalizada && ganadorId == null -> "Si la partida está finalizada, debe seleccionar un ganador"
            ganadorId != null && ganadorId != jugador1Id && ganadorId != jugador2Id ->
                "El ganador debe ser uno de los jugadores de la partida"
            else -> null
        }
    }
}