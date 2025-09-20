package edu.ucne.registrojugadoresmv.domain.repository

import edu.ucne.registrojugadoresmv.domain.model.Partida
import kotlinx.coroutines.flow.Flow

interface PartidaRepository {
    fun getPartidas(): Flow<List<Partida>>
    suspend fun getPartida(id: Int): Partida?
    suspend fun savePartida(partida: Partida)
    suspend fun deletePartida(partida: Partida)
    suspend fun deletePartidaById(id: Int)
}