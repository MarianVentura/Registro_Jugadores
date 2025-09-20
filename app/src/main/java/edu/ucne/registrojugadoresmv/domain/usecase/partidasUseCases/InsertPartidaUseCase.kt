package edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases

import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import javax.inject.Inject

class InsertPartidaUseCase @Inject constructor(
    private val repository: PartidaRepository,
    private val validatePartidaUseCase: ValidatePartidaUseCase
) {
    suspend operator fun invoke(partida: Partida): Result<Unit> {
        return try {
            // Realizar validaciones usando ValidatePartidaUseCase
            val jugador1Validation = validatePartidaUseCase.validateJugador1(partida.jugador1Id)
            if (jugador1Validation != null) {
                return Result.failure(Exception(jugador1Validation))
            }

            val jugador2Validation = validatePartidaUseCase.validateJugador2(partida.jugador2Id)
            if (jugador2Validation != null) {
                return Result.failure(Exception(jugador2Validation))
            }

            val jugadoresDiferentesValidation = validatePartidaUseCase.validateJugadoresDiferentes(
                partida.jugador1Id, partida.jugador2Id
            )
            if (jugadoresDiferentesValidation != null) {
                return Result.failure(Exception(jugadoresDiferentesValidation))
            }

            val ganadorValidation = validatePartidaUseCase.validateGanador(
                partida.esFinalizada, partida.ganadorId, partida.jugador1Id, partida.jugador2Id
            )
            if (ganadorValidation != null) {
                return Result.failure(Exception(ganadorValidation))
            }

            // Si todas las validaciones pasan, guardar la partida
            repository.savePartida(partida)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}