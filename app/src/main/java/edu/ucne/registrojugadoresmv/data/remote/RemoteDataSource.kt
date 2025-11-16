package edu.ucne.registrojugadoresmv.data.remote

import edu.ucne.registrojugadoresmv.data.remote.dto.MovimientoDto
import edu.ucne.registrojugadoresmv.data.remote.jugadores.JugadorRequest
import edu.ucne.registrojugadoresmv.data.remote.jugadores.JugadorResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val TicTacToeApi: TicTacToeApi
) {
    //Movimientos
    suspend fun getMovimientos(partidaId: Int): List<MovimientoDto> =
        TicTacToeApi.getMovimientos(partidaId)

    suspend fun saveMovimiento(movimientoDto: MovimientoDto) =
        TicTacToeApi.saveMovimiento(movimientoDto)

    //Jugadores
    suspend fun getJugadores(): Resource<List<JugadorResponse>> {
        return try {
            val response = TicTacToeApi.getJugadores()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun createJugador(request: JugadorRequest): Resource<JugadorResponse> {
        return try {
            val response = TicTacToeApi.createJugador(request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun updateJugador(id: Int, request: JugadorRequest): Resource<Unit> {
        return try {
            val response = TicTacToeApi.updateJugador(id, request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun deleteJugador(id: Int): Resource<Unit> {
        return try {
            val response = TicTacToeApi.deleteJugador(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }
}

