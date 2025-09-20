package edu.ucne.registrojugadoresmv.data.repository

import edu.ucne.registrojugadoresmv.data.local.dao.PartidaDao
import edu.ucne.registrojugadoresmv.data.mappers.toEntity
import edu.ucne.registrojugadoresmv.data.mappers.toModel
import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartidaRepositoryImpl @Inject constructor(
    private val partidaDao: PartidaDao
) : PartidaRepository {

    override fun getPartidas(): Flow<List<Partida>> =
        partidaDao.getAll().map { entities ->
            entities.map { it.toModel() }
        }

    override suspend fun getPartida(id: Int): Partida? =
        partidaDao.find(id)?.toModel()

    override suspend fun savePartida(partida: Partida) {
        partidaDao.save(partida.toEntity())
    }

    override suspend fun deletePartida(partida: Partida) {
        partidaDao.delete(partida.toEntity())
    }

    override suspend fun deletePartidaById(id: Int) {
        partidaDao.deleteById(id)
    }
}