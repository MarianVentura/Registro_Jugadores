package edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases

import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class ValidatePartidaUseCase @Inject constructor(
    private val jugadorRepository: JugadorRepository
) {

    suspend fun validateJugador1(jugadorId: Int): String? {
        return when {
            jugadorId <= 0 -> "Debe seleccionar el jugador 1"
            jugadorRepository.getJugador(jugadorId) == null -> "El jugador 1 seleccionado no existe"
            else -> null
        }
    }

    suspend fun validateJugador2(jugadorId: Int): String? {
        return when {
            jugadorId <= 0 -> "Debe seleccionar el jugador 2"
            jugadorRepository.getJugador(jugadorId) == null -> "El jugador 2 seleccionado no existe"
            else -> null
        }
    }

    fun validateJugadoresDiferentes(jugador1Id: Int, jugador2Id: Int): String? {
        return if (jugador1Id == jugador2Id) {
            "Los jugadores deben ser diferentes"
        } else null
    }

    suspend fun validateGanador(
        esFinalizada: Boolean,
        ganadorId: Int?,
        jugador1Id: Int,
        jugador2Id: Int
    ): String? {
        return when {
            esFinalizada && ganadorId == null -> "Debe seleccionar un ganador para partidas finalizadas"
            esFinalizada && ganadorId != null && ganadorId != 0 &&
                    ganadorId != jugador1Id && ganadorId != jugador2Id ->
                "El ganador debe ser uno de los jugadores participantes o empate (0)"
            ganadorId != null && ganadorId > 0 &&
                    jugadorRepository.getJugador(ganadorId) == null ->
                "El jugador ganador seleccionado no existe"
            else -> null
        }
    }

    // Método de validación completa
    suspend fun validatePartida(
        jugador1Id: Int,
        jugador2Id: Int,
        ganadorId: Int?,
        esFinalizada: Boolean
    ): List<String> {
        val errors = mutableListOf<String>()

        validateJugador1(jugador1Id)?.let { errors.add(it) }
        validateJugador2(jugador2Id)?.let { errors.add(it) }
        validateJugadoresDiferentes(jugador1Id, jugador2Id)?.let { errors.add(it) }
        validateGanador(esFinalizada, ganadorId, jugador1Id, jugador2Id)?.let { errors.add(it) }

        return errors
    }
}