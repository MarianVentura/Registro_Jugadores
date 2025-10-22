package edu.ucne.registrojugadoresmv.data.repository

import edu.ucne.registrojugadoresmv.data.local.dao.LogroDao
import edu.ucne.registrojugadoresmv.data.mappers.toDomain
import edu.ucne.registrojugadoresmv.data.mappers.toEntity
import edu.ucne.registrojugadoresmv.domain.model.Logro
import edu.ucne.registrojugadoresmv.domain.repository.LogroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogroRepositoryImpl @Inject constructor(
    private val logroDao: LogroDao
) : LogroRepository {

    override suspend fun insertLogro(logro: Logro): Long {
        return logroDao.insert(logro.toEntity())
    }

    override suspend fun updateLogro(logro: Logro) {
        logroDao.update(logro.toEntity())
    }

    override suspend fun deleteLogro(logro: Logro) {
        logroDao.delete(logro.toEntity())
    }

    override fun getAllLogros(): Flow<List<Logro>> {
        return logroDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getLogroById(id: Int): Logro? {
        return logroDao.getById(id)?.toDomain()
    }

    override suspend fun existeNombre(nombre: String): Boolean {
        return logroDao.existeNombre(nombre) > 0
    }
}