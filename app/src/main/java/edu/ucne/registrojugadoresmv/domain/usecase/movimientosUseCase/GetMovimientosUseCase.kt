package edu.ucne.registrojugadoresmv.domain.usecase.movimientos

import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Movimiento
import edu.ucne.registrojugadoresmv.domain.repository.MovimientoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovimientosUseCase @Inject constructor(
    private val repository: MovimientoRepository
) {
    operator fun invoke(partidaId: Int): Flow<Resource<List<Movimiento>>> {
        return repository.getMovimientos(partidaId)
    }
}