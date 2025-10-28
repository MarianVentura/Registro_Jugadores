package edu.ucne.registrojugadoresmv.data.remote

import edu.ucne.registrojugadoresmv.data.remote.dto.MovimientoDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val movimientoApi: MovimientoApi
) {
    suspend fun getMovimientos(partidaId: Int): List<MovimientoDto> =
        movimientoApi.getMovimientos(partidaId)

    suspend fun saveMovimiento(movimientoDto: MovimientoDto) =
        movimientoApi.saveMovimiento(movimientoDto)
}
