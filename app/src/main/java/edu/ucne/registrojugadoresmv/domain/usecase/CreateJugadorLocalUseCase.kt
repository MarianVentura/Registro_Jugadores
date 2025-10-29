package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class CreateJugadorLocalUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(jugador: Jugador): Resource<Jugador> {
        return repository.createJugadorLocal(jugador)
    }
}