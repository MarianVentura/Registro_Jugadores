package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class ValidateJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend fun validateNombre(nombre: String, excludeId: String? = null): String? {
        return when {
            nombre.isBlank() -> "El nombre es obligatorio"
            repository.existeNombre(nombre.trim(), excludeId?.toIntOrNull()) -> "Ya existe un jugador con ese nombre"
            else -> null
        }
    }

    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "El email es obligatorio"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Email inválido"
            else -> null
        }
    }
}