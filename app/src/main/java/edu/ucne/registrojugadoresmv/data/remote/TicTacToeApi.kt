package edu.ucne.registrojugadoresmv.data.remote

import edu.ucne.registrojugadoresmv.data.remote.dto.MovimientoDto
import edu.ucne.registrojugadoresmv.data.remote.jugadores.JugadorRequest
import edu.ucne.registrojugadoresmv.data.remote.jugadores.JugadorResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path

interface TicTacToeApi {
    @GET("api/Movimientos/{partidaId}")
    suspend fun getMovimientos(@Path("partidaId") partidaId: Int): List<MovimientoDto>

    @POST("api/Movimientos")
    suspend fun saveMovimiento(@Body movimientoDto: MovimientoDto)

    @GET("api/Jugadores")
    suspend fun getJugadores(): Response<List<JugadorResponse>>

    @POST("api/Jugadores")
    suspend fun createJugador(@Body request: JugadorRequest): Response<JugadorResponse>

    @PUT("api/Jugadores/{id}")
    suspend fun updateJugador(
        @Path("id") id: Int,
        @Body request: JugadorRequest
    ): Response<Unit>

    @DELETE("api/Jugadores/{id}")
    suspend fun deleteJugador(@Path("id") id: Int): Response<Unit>
}