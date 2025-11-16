package edu.ucne.registrojugadoresmv.data.local.dao

import androidx.room.*
import edu.ucne.registrojugadoresmv.data.local.entities.JugadorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadorDao {
    @Upsert
    suspend fun upsert(jugador: JugadorEntity)

    @Delete
    suspend fun delete(jugador: JugadorEntity)

    @Query("SELECT * FROM Jugadores ORDER BY nombres ASC")
    fun getAll(): Flow<List<JugadorEntity>>

    @Query("SELECT * FROM Jugadores WHERE id = :id")
    suspend fun getById(id: String): JugadorEntity?

    @Query("SELECT * FROM Jugadores WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int): JugadorEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM Jugadores WHERE LOWER(TRIM(nombres)) = LOWER(TRIM(:nombre)) AND (:excludeId IS NULL OR id != :excludeId))")
    suspend fun existeNombre(nombre: String, excludeId: String? = null): Boolean

    @Query("SELECT * FROM Jugadores WHERE isPendingCreate = 1")
    suspend fun getPendingCreateJugadores(): List<JugadorEntity>

    @Query("DELETE FROM Jugadores WHERE id = :id")
    suspend fun deleteById(id: String)
}