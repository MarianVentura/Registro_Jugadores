package edu.ucne.registrojugadoresmv.domain.repository

import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Movimiento
import kotlinx.coroutines.flow.Flow

interface MovimientoRepository {
    fun getMovimientos(partidaId: Int): Flow<Resource<List<Movimiento>>>
    suspend fun saveMovimiento(movimiento: Movimiento): Resource<Unit>
}