package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class UpdateJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository,
    private val validateJugadorUseCase: ValidateJugadorUseCase
) {
    suspend operator fun invoke(jugador: Jugador): Resource<Unit> {
        val nombresError = validateJugadorUseCase.validateNombre(jugador.nombres, jugador.id)
        if (nombresError != null) {
            return Resource.Error(nombresError)
        }

        val emailError = validateJugadorUseCase.validateEmail(jugador.email)
        if (emailError != null) {
            return Resource.Error(emailError)
        }

        return repository.upsert(jugador.copy(nombres = jugador.nombres.trim()))
    }
}