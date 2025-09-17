package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository

class UpdateJugadorUseCase(
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

            // Verificar que no existe otro jugador con el mismo nombre
            val existingJugadores = repository.getAllJugadores()
            // Esta verificación requeriría modificar el repository para obtener una lista sincrónica
            // Por simplicidad, asumiremos que la validación se hace en el ViewModel

            repository.updateJugador(jugador.copy(nombres = jugador.nombres.trim()))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}