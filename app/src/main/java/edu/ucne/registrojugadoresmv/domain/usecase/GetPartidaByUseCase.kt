package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import javax.inject.Inject

class GetPartidaByIdUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    suspend operator fun invoke(id: Int): Partida? {
        return repository.getPartidaById(id)
    }
}