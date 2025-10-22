package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class UpdateJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(jugador: Jugador): Result<Unit> {
        return try {
            // Validaciones
            if (jugador.nombres.isBlank()) {
                return Result.failure(Exception("El nombre es obligatorio"))
            }

            if (jugador.partidas < 0) {
                return Result.failure(Exception("Las partidas no pueden ser negativas"))
            }

            repository.updateJugador(jugador.copy(nombres = jugador.nombres.trim()))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}