package edu.ucne.registrojugadoresmv.data.local.dao

import androidx.room.*
import edu.ucne.registrojugadoresmv.data.local.entities.Jugador
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadorDao {
    @Insert
    suspend fun insert(jugador: Jugador): Long

    @Update
    suspend fun update(jugador: Jugador)

    @Delete
    suspend fun delete(jugador: Jugador)

    @Query("SELECT * FROM Jugadores")
    fun getAll(): Flow<List<Jugador>>

    @Query("SELECT * FROM Jugadores WHERE jugadorId = :id")
    suspend fun getById(id: Int): Jugador?

    @Query("SELECT COUNT(*) FROM Jugadores WHERE nombres = :nombre")
    suspend fun existeNombre(nombre: String): Int
}