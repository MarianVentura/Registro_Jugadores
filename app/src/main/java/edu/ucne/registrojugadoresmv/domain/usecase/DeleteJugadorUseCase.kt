package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class DeleteJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(id: Int): Resource<Unit> {
        repository.deleteById(id)
        return Resource.Success(Unit)
    }

    suspend operator fun invoke(id: String): Resource<Unit> {
        return repository.delete(id)
    }

    suspend operator fun invoke(jugador: Jugador): Resource<Unit> {
        return repository.delete(jugador)
    }
}