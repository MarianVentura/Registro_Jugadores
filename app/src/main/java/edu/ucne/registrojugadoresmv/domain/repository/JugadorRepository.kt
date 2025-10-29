package edu.ucne.registrojugadoresmv.domain.repository

import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Jugador
import kotlinx.coroutines.flow.Flow

interface JugadorRepository {
    fun observeJugadores(): Flow<List<Jugador>>
    suspend fun getJugador(id: String): Jugador?
    suspend fun getJugador(id: Int?): Jugador?
    suspend fun createJugadorLocal(jugador: Jugador): Resource<Jugador>
    suspend fun upsert(jugador: Jugador): Resource<Unit>
    suspend fun delete(jugador: Jugador): Resource<Unit>
    suspend fun deleteById(id: Int)
    suspend fun delete(id: String): Resource<Unit>
    suspend fun existeNombre(nombre: String, excludeId: Int? = null): Boolean
    suspend fun postPendingJugadores(): Resource<Unit>
    suspend fun syncJugadores(): Resource<Unit>
}


