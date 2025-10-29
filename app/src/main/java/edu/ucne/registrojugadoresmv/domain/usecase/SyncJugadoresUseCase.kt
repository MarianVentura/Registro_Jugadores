package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Inject

class SyncJugadoresUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        val syncResult = repository.syncJugadores()

        return repository.postPendingJugadores()
    }
}