package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import javax.inject.Inject

class UpdatePartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    suspend operator fun invoke(partida: Partida): Result<Unit> {
        return try {
            // Mismas validaciones que el insert
            if (partida.jugador1Id == partida.jugador2Id) {
                return Result.failure(Exception("Un jugador no puede jugar contra sí mismo"))
            }

            if (partida.fecha.isBlank()) {
                return Result.failure(Exception("La fecha es obligatoria"))
            }

            if (partida.esFinalizada && partida.ganadorId == null) {
                return Result.failure(Exception("Si la partida está finalizada, debe tener un ganador"))
            }

            if (partida.ganadorId != null &&
                partida.ganadorId != partida.jugador1Id &&
                partida.ganadorId != partida.jugador2Id) {
                return Result.failure(Exception("El ganador debe ser uno de los jugadores de la partida"))
            }

            repository.updatePartida(partida)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
