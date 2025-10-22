package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class ValidateJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend fun validateNombre(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre es obligatorio"
            repository.existeNombre(nombre.trim()) -> "Ya existe un jugador con ese nombre"
            else -> null
        }
    }

    fun validatePartidas(partidas: String): String? {
        return when {
            partidas.isBlank() -> "Las partidas son obligatorias"
            partidas.toIntOrNull() == null -> "Las partidas deben ser un número válido"
            partidas.toInt() < 0 -> "Las partidas no pueden ser negativas"
            else -> null
        }
    }
}