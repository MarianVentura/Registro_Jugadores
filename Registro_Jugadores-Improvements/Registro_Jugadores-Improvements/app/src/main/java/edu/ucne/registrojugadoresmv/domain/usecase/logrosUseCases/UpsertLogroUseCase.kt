package edu.ucne.registrojugadoresmv.domain.usecase.logrosUseCases

import edu.ucne.registrojugadoresmv.domain.model.Logro
import edu.ucne.registrojugadoresmv.domain.repository.LogroRepository
import javax.inject.Inject

class UpsertLogroUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    suspend operator fun invoke(logro: Logro): Result<Long> {
        return runCatching {
            if (logro.logroId == 0) {
                repository.insertLogro(logro)
            } else {
                repository.updateLogro(logro)
                logro.logroId.toLong()
            }
        }
    }
}