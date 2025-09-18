package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import javax.inject.Inject

class InsertPartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    suspend operator fun invoke(partida: Partida): Result<Long> {
        return try {
            // Validaciones de negocio
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

            val id = repository.insertPartida(partida)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}