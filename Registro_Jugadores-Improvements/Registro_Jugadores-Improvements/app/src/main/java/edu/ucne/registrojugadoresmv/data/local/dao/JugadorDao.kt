package edu.ucne.registrojugadoresmv.data.local.dao

import androidx.room.*
import edu.ucne.registrojugadoresmv.data.local.entities.JugadorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadorDao {
    @Insert
    suspend fun insert(jugador: JugadorEntity): Long

    @Update
    suspend fun update(jugador: JugadorEntity)

    @Delete
    suspend fun delete(jugador: JugadorEntity)

    @Query("SELECT * FROM Jugadores")
    fun getAll(): Flow<List<JugadorEntity>>

    @Query("SELECT * FROM Jugadores WHERE jugadorId = :id")
    suspend fun getById(id: Int): JugadorEntity?

    @Query("SELECT COUNT(*) FROM Jugadores WHERE nombres = :nombre")
    suspend fun existeNombre(nombre: String): Int
}