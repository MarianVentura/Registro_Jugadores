package edu.ucne.registrojugadoresmv.data.repository

import edu.ucne.registrojugadoresmv.data.local.dao.JugadorDao
import edu.ucne.registrojugadoresmv.data.mappers.toDomain
import edu.ucne.registrojugadoresmv.data.mappers.toEntity
import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class JugadorRepositoryImpl(
    private val jugadorDao: JugadorDao
) : JugadorRepository {

    override suspend fun insertJugador(jugador: Jugador): Long {
        return jugadorDao.insert(jugador.toEntity())
    }

    override suspend fun updateJugador(jugador: Jugador) {
        jugadorDao.update(jugador.toEntity())
    }

    override suspend fun deleteJugador(jugador: Jugador) {
        jugadorDao.delete(jugador.toEntity())
    }

    override fun getAllJugadores(): Flow<List<Jugador>> {
        return jugadorDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getJugadorById(id: Int): Jugador? {
        return jugadorDao.getById(id)?.toDomain()
    }

    override suspend fun existeNombre(nombre: String): Boolean {
        return jugadorDao.existeNombre(nombre) > 0
    }
}