package edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases

import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import javax.inject.Inject

class DeletePartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    suspend operator fun invoke(partidaId: Int) {
        repository.deleteById(partidaId)
    }
}

