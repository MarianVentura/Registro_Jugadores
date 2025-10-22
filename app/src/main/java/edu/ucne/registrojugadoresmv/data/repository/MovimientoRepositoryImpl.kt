package edu.ucne.registrojugadoresmv.data.remote.repository

import edu.ucne.registrojugadoresmv.data.mappers.toDomain
import edu.ucne.registrojugadoresmv.data.mappers.toDto
import edu.ucne.registrojugadoresmv.data.remote.MovimientoApi
import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Movimiento
import edu.ucne.registrojugadoresmv.domain.repository.MovimientoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovimientoRepositoryImpl @Inject constructor(
    private val movimientoApi: MovimientoApi
) : MovimientoRepository {

    override fun getMovimientos(partidaId: Int): Flow<Resource<List<Movimiento>>> = flow {
        try {
            emit(Resource.Loading())
            val movimientosDto = movimientoApi.getMovimientos(partidaId)
            val movimientos = movimientosDto.map { it.toDomain() }
            emit(Resource.Success(movimientos))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP desconocido"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de conexión. Verifica tu internet."))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    override suspend fun saveMovimiento(movimiento: Movimiento): Resource<Unit> {
        return try {
            movimientoApi.saveMovimiento(movimiento.toDto())
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error(e.message ?: "Error HTTP desconocido")
        } catch (e: IOException) {
            Resource.Error("Error de conexión. Verifica tu internet.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error desconocido")
        }
    }
}