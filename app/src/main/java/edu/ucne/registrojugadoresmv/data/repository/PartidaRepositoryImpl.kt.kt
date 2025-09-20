package edu.ucne.registrojugadoresmv.data.repository

import edu.ucne.registrojugadoresmv.data.local.dao.PartidaDao
import edu.ucne.registrojugadoresmv.data.mappers.toDomain
import edu.ucne.registrojugadoresmv.data.mappers.toEntity
import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartidaRepositoryImpl @Inject constructor(
    private val dao: PartidaDao
) : PartidaRepository {

    override fun observePartida(): Flow<List<Partida>> =
        dao.ObserveAll().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getPartida(id: Int?): Partida? =
        dao.getById(id)?.toDomain()

    override suspend fun upsert(partida: Partida): Int {
        dao.upsert(partida.toEntity())
        return partida.partidaId
    }

    override suspend fun delete(partida: Partida) {
        dao.delete(partida.toEntity())
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}