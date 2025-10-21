package edu.ucne.registrojugadoresmv.domain.usecase

import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import kotlinx.coroutines.flow.Flow

class GetJugadoresUseCase(
    private val repository: JugadorRepository
) {
    operator fun invoke(): Flow<List<Jugador>> {
        return repository.getAllJugadores()
    }
}