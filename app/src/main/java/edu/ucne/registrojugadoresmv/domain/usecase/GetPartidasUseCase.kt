package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.model.PartidaConNombres
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPartidasUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    operator fun invoke(): Flow<List<PartidaConNombres>> {
        return repository.getPartidasConNombres()
    }
}