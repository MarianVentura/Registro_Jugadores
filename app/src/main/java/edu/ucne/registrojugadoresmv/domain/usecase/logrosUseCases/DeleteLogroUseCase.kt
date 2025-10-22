package edu.ucne.registrojugadoresmv.domain.usecase.logrosUseCases

import edu.ucne.registrojugadoresmv.domain.repository.LogroRepository
import javax.inject.Inject

class DeleteLogroUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    suspend operator fun invoke(logroId: Int) {
        val logro = repository.getLogroById(logroId)
        logro?.let {
            repository.deleteLogro(it)
        }
    }
}