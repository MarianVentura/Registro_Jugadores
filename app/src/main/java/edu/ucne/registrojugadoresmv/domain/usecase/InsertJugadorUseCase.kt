package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class InsertJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(jugador: Jugador): Result<Long> {
        return try {
            if (jugador.nombres.isBlank()) {
                return Result.failure(Exception("El nombre es obligatorio"))
            }

            if (jugador.partidas < 0) {
                return Result.failure(Exception("Las partidas no pueden ser negativas"))
            }

            if (repository.existeNombre(jugador.nombres.trim())) {
                return Result.failure(Exception("Ya existe un jugador con ese nombre"))
            }

            val id = repository.insertJugador(
                jugador.copy(nombres = jugador.nombres.trim())
            )
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}