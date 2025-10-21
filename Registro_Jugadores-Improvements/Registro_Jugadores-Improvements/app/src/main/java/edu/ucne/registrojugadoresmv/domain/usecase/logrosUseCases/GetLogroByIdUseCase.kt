package edu.ucne.registrojugadoresmv.domain.usecase.logrosUseCases

import edu.ucne.registrojugadoresmv.domain.model.Logro
import edu.ucne.registrojugadoresmv.domain.repository.LogroRepository
import javax.inject.Inject

class GetLogroByIdUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    suspend operator fun invoke(id: Int): Logro? = repository.getLogroById(id)
}