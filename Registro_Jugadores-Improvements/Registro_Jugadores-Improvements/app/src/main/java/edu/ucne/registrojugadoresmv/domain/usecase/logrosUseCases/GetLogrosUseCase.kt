package edu.ucne.registrojugadoresmv.domain.usecase.logrosUseCases

import edu.ucne.registrojugadoresmv.domain.model.Logro
import edu.ucne.registrojugadoresmv.domain.repository.LogroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLogrosUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    operator fun invoke(): Flow<List<Logro>> = repository.getAllLogros()
}