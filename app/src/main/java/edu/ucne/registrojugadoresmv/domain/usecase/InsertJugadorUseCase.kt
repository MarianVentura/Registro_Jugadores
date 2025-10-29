package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class InsertJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository,
    private val validateJugadorUseCase: ValidateJugadorUseCase
) {
    suspend operator fun invoke(jugador: Jugador): Resource<Jugador> {
        val nombresError = validateJugadorUseCase.validateNombre(jugador.nombres)
        if (nombresError != null) {
            return Resource.Error(nombresError)
        }

        val emailError = validateJugadorUseCase.validateEmail(jugador.email)
        if (emailError != null) {
            return Resource.Error(emailError)
        }

        if (repository.existeNombre(jugador.nombres.trim())) {
            return Resource.Error("Ya existe un jugador con ese nombre")
        }

        return repository.createJugadorLocal(jugador.copy(nombres = jugador.nombres.trim()))
    }
}