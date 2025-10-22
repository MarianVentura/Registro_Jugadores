package edu.ucne.registrojugadoresmv.data.local.dao

import androidx.room.*
import edu.ucne.registrojugadoresmv.data.local.entities.LogroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogroDao {
    @Insert
    suspend fun insert(logro: LogroEntity): Long

    @Update
    suspend fun update(logro: LogroEntity)

    @Delete
    suspend fun delete(logro: LogroEntity)

    @Query("SELECT * FROM Logros ORDER BY logroId DESC")
    fun getAll(): Flow<List<LogroEntity>>

    @Query("SELECT * FROM Logros WHERE logroId = :id")
    suspend fun getById(id: Int): LogroEntity?

    @Query("SELECT COUNT(*) FROM Logros WHERE nombre = :nombre")
    suspend fun existeNombre(nombre: String): Int
}