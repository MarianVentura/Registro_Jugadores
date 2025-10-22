package edu.ucne.registrojugadoresmv.domain.repository

import edu.ucne.registrojugadoresmv.domain.model.Logro
import kotlinx.coroutines.flow.Flow

interface LogroRepository {
    suspend fun insertLogro(logro: Logro): Long
    suspend fun updateLogro(logro: Logro)
    suspend fun deleteLogro(logro: Logro)
    fun getAllLogros(): Flow<List<Logro>>
    suspend fun getLogroById(id: Int): Logro?
    suspend fun existeNombre(nombre: String): Boolean
}