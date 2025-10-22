package edu.ucne.registrojugadoresmv.domain.usecase.movimientos

import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Movimiento
import edu.ucne.registrojugadoresmv.domain.repository.MovimientoRepository
import javax.inject.Inject

class SaveMovimientoUseCase @Inject constructor(
    private val repository: MovimientoRepository
) {
    suspend operator fun invoke(movimiento: Movimiento): Resource<Unit> {
        return repository.saveMovimiento(movimiento)
    }
}