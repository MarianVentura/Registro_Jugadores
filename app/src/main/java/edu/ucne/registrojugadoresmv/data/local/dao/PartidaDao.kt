package edu.ucne.registrojugadoresmv.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrojugadoresmv.data.local.entities.PartidaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {
    @Query("SELECT * FROM Partidas ORDER BY partidaId DESC")
    fun getAll(): Flow<List<PartidaEntity>>

    @Query("SELECT * FROM Partidas WHERE partidaId = :id")
    suspend fun find(id: Int): PartidaEntity?

    @Upsert
    suspend fun save(partida: PartidaEntity)

    @Delete
    suspend fun delete(partida: PartidaEntity)

    @Query("DELETE FROM Partidas WHERE partidaId = :id")
    suspend fun deleteById(id: Int)
}