package edu.ucne.registrojugadoresmv.data.remote

import edu.ucne.registrojugadoresmv.data.remote.dto.MovimientoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MovimientoApi {
    @GET("api/Movimientos/{partidaId}")
    suspend fun getMovimientos(@Path("partidaId") partidaId: Int): List<MovimientoDto>

    @POST("api/Movimientos")
    suspend fun saveMovimiento(@Body movimientoDto: MovimientoDto)
}