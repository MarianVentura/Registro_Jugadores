package edu.ucne.registrojugadoresmv.data.repository

import edu.ucne.registrojugadoresmv.data.local.dao.JugadorDao
import edu.ucne.registrojugadoresmv.data.mappers.toDomain
import edu.ucne.registrojugadoresmv.data.mappers.toEntity
import edu.ucne.registrojugadoresmv.data.mappers.toRequest
import edu.ucne.registrojugadoresmv.data.remote.RemoteDataSource
import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JugadorRepositoryImpl @Inject constructor(
    private val localDataSource: JugadorDao,
    private val remoteDataSource: RemoteDataSource
) : JugadorRepository {

    override fun observeJugadores(): Flow<List<Jugador>> =
        localDataSource.getAll().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getJugador(id: String): Jugador? =
        localDataSource.getById(id)?.toDomain()

    override suspend fun getJugador(id: Int?): Jugador? {
        if (id == null) return null
        return localDataSource.getByRemoteId(id)?.toDomain()
    }

    override suspend fun createJugadorLocal(jugador: Jugador): Resource<Jugador> {
        val pending = jugador.copy(isPendingCreate = true)
        localDataSource.upsert(pending.toEntity())
        return Resource.Success(pending)
    }

    override suspend fun upsert(jugador: Jugador): Resource<Unit> {
        val remoteId = jugador.remoteId ?: return Resource.Error("No remoteId")
        val request = jugador.toRequest()

        return when (val result = remoteDataSource.updateJugador(remoteId, request)) {
            is Resource.Success -> {
                localDataSource.upsert(jugador.toEntity())
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al actualizar")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun delete(jugador: Jugador): Resource<Unit> {
        val remoteId = jugador.remoteId ?: return Resource.Error("No remoteId")

        return when (val result = remoteDataSource.deleteJugador(remoteId)) {
            is Resource.Success -> {
                localDataSource.delete(jugador.toEntity())
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al eliminar")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun deleteById(id: Int) {
        val jugador = localDataSource.getByRemoteId(id)
        if (jugador != null) {
            localDataSource.deleteById(jugador.id)
        }
    }

    override suspend fun delete(id: String): Resource<Unit> {
        val jugador = localDataSource.getById(id) ?: return Resource.Error("No encontrado")
        val remoteId = jugador.remoteId ?: return Resource.Error("No remoteId")

        return when (val result = remoteDataSource.deleteJugador(remoteId)) {
            is Resource.Success -> {
                localDataSource.deleteById(id)
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al eliminar")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun existeNombre(nombre: String, excludeId: Int?): Boolean {
        return localDataSource.existeNombre(nombre, excludeId?.toString())
    }

    override suspend fun postPendingJugadores(): Resource<Unit> {
        val pending = localDataSource.getPendingCreateJugadores()

        android.util.Log.d("JugadorRepository", "📤 Jugadores pendientes: ${pending.size}")

        for (jugador in pending) {
            android.util.Log.d("JugadorRepository", "📤 Enviando: ${jugador.nombres} (${jugador.email})")
            val request = jugador.toDomain().toRequest()

            when (val result = remoteDataSource.createJugador(request)) {
                is Resource.Success -> {
                    android.util.Log.d("JugadorRepository", "✅ Servidor respondió: jugadorId=${result.data?.jugadorId}")
                    val synced = jugador.copy(
                        remoteId = result.data?.jugadorId,
                        isPendingCreate = false
                    )
                    localDataSource.upsert(synced)
                    android.util.Log.d("JugadorRepository", "✅ Actualizado en Room: remoteId=${synced.remoteId}")
                }
                is Resource.Error -> {
                    android.util.Log.e("JugadorRepository", "❌ Error: ${result.message}")
                    return Resource.Error("Falló la sincronización")
                }
                is Resource.Loading -> {}
            }
        }

        android.util.Log.d("JugadorRepository", "✅ Sincronización completada")
        return Resource.Success(Unit)
    }

    override suspend fun syncJugadores(): Resource<Unit> {
        return when (val result = remoteDataSource.getJugadores()) {
            is Resource.Success -> {
                result.data?.forEach { jugadorResponse ->
                    val existingJugador = localDataSource.getByRemoteId(jugadorResponse.jugadorId)
                    if (existingJugador == null) {
                        val jugadorEntity = jugadorResponse.toDomain().toEntity()
                        localDataSource.upsert(jugadorEntity)
                    }
                }
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al sincronizar")
            is Resource.Loading -> Resource.Loading()
        }
    }
}