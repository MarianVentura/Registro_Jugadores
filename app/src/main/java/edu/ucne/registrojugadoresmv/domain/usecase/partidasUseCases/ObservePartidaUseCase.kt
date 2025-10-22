package edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases

import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    operator fun invoke(): Flow<List<Partida>> = repository.observePartida()
}