package edu.ucne.registrojugadoresmv.domain.usecase.logrosUseCases

import edu.ucne.registrojugadoresmv.domain.repository.LogroRepository
import javax.inject.Inject

class ValidateLogroUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    suspend fun validateNombre(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre es obligatorio"
            repository.existeNombre(nombre.trim()) -> "Ya existe un logro con ese nombre"
            else -> null
        }
    }

    fun validateDescripcion(descripcion: String): String? {
        return when {
            descripcion.isBlank() -> "La descripción es obligatoria"
            descripcion.length < 10 -> "La descripción debe tener al menos 10 caracteres"
            descripcion.length > 500 -> "La descripción no puede exceder 500 caracteres"
            else -> null
        }
    }
}