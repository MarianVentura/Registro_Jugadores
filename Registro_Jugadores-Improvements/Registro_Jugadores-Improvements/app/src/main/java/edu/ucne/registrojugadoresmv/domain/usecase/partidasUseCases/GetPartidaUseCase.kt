package edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases

import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import javax.inject.Inject

class GetPartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    suspend operator fun invoke(id: Int): Partida? = repository.getPartida(id)
}