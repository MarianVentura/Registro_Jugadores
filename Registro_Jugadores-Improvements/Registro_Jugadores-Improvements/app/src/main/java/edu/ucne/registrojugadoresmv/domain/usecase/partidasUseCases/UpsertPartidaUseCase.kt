package edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases

import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import javax.inject.Inject

class UpsertPartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    suspend operator fun invoke(partida: Partida): Result<Int> {
        val j1Result =
            validateJugador1(partida.jugador1Id.toString(), partida.jugador2Id.toString())
        val j2Result =
            validateJugador2(partida.jugador2Id.toString(), partida.jugador1Id.toString())

        if (!j1Result.isValid) {
            return Result.failure(IllegalArgumentException("Jugador 1: ${j1Result.error}"))
        }
        if (!j2Result.isValid) {
            return Result.failure(IllegalArgumentException("Jugador 2: ${j2Result.error}"))
        }

        return runCatching { repository.upsert(partida) }
    }
}