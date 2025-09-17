package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import javax.inject.Inject

class DeletePartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    suspend operator fun invoke(partida: Partida): Result<Unit> {
        return try {
            repository.deletePartida(partida)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}