package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class GetJugadorByIdUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(id: Int): Jugador? {
        return repository.getJugadorById(id)
    }
}