package edu.ucne.registrojugadoresmv.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrojugadoresmv.data.local.entities.PartidaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {
    @Query(value = "SELECT * FROM Partidas ORDER BY partidaId DESC")
    fun ObserveAll(): Flow<List<PartidaEntity>>

    @Query(value = "SELECT * FROM Partidas WHERE partidaId = :id")
    suspend fun getById(id: Int?): PartidaEntity?

    @Upsert
    suspend fun upsert(entity: PartidaEntity)

    @Delete
    suspend fun delete(entity: PartidaEntity)

    @Query(value = "DELETE FROM Partidas WHERE partidaId = :id")
    suspend fun deleteById(id: Int)
}