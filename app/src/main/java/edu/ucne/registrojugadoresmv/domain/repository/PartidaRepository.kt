package edu.ucne.registrojugadoresmv.domain.repository

import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.model.PartidaConNombres
import kotlinx.coroutines.flow.Flow

interface PartidaRepository {
    suspend fun insertPartida(partida: Partida): Long
    suspend fun updatePartida(partida: Partida)
    suspend fun deletePartida(partida: Partida)
    fun getAllPartidas(): Flow<List<Partida>>
    fun getPartidasConNombres(): Flow<List<PartidaConNombres>>
    suspend fun getPartidaById(id: Int): Partida?
    suspend fun getPartidasFinalizadasPorJugador(jugadorId: Int): Int
    suspend fun getVictoriasPorJugador(jugadorId: Int): Int
}