package edu.ucne.registrojugadoresmv.data.repository

import edu.ucne.registrojugadoresmv.data.local.dao.PartidaDao
import edu.ucne.registrojugadoresmv.data.mappers.toDomain
import edu.ucne.registrojugadoresmv.data.mappers.toEntity
import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.model.PartidaConNombres
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartidaRepositoryImpl @Inject constructor(
    private val partidaDao: PartidaDao
) : PartidaRepository {

    override suspend fun insertPartida(partida: Partida): Long {
        return partidaDao.insert(partida.toEntity())
    }

    override suspend fun updatePartida(partida: Partida) {
        partidaDao.update(partida.toEntity())
    }

    override suspend fun deletePartida(partida: Partida) {
        partidaDao.delete(partida.toEntity())
    }

    override fun getAllPartidas(): Flow<List<Partida>> {
        return partidaDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPartidasConNombres(): Flow<List<PartidaConNombres>> {
        return partidaDao.getPartidasConNombres().map { dtos ->
            dtos.map { it.toDomain() }
        }
    }

    override suspend fun getPartidaById(id: Int): Partida? {
        return partidaDao.getById(id)?.toDomain()
    }

    override suspend fun getPartidasFinalizadasPorJugador(jugadorId: Int): Int {
        return partidaDao.getPartidasFinalizadasPorJugador(jugadorId)
    }

    override suspend fun getVictoriasPorJugador(jugadorId: Int): Int {
        return partidaDao.getVictoriasPorJugador(jugadorId)
    }
}