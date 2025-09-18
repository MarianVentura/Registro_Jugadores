package edu.ucne.registrojugadoresmv.data.local.dao

import androidx.room.*
import edu.ucne.registrojugadoresmv.data.local.entities.Partida
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {
    @Insert
    suspend fun insert(partida: Partida): Long

    @Update
    suspend fun update(partida: Partida)

    @Delete
    suspend fun delete(partida: Partida)

    @Query("SELECT * FROM Partidas ORDER BY fecha DESC")
    fun getAll(): Flow<List<Partida>>

    @Query("SELECT * FROM Partidas WHERE partidaId = :id")
    suspend fun getById(id: Int): Partida?

    @Query("""
        SELECT p.*, 
               j1.nombres as jugador1Nombre, 
               j2.nombres as jugador2Nombre,
               g.nombres as ganadorNombre
        FROM Partidas p
        INNER JOIN Jugadores j1 ON p.jugador1Id = j1.jugadorId
        INNER JOIN Jugadores j2 ON p.jugador2Id = j2.jugadorId
        LEFT JOIN Jugadores g ON p.ganadorId = g.jugadorId
        ORDER BY p.fecha DESC
    """)
    fun getPartidasConNombres(): Flow<List<PartidaConNombresDto>>

    @Query("SELECT COUNT(*) FROM Partidas WHERE (jugador1Id = :jugadorId OR jugador2Id = :jugadorId) AND esFinalizada = 1")
    suspend fun getPartidasFinalizadasPorJugador(jugadorId: Int): Int

    @Query("SELECT COUNT(*) FROM Partidas WHERE ganadorId = :jugadorId")
    suspend fun getVictoriasPorJugador(jugadorId: Int): Int
}

// DTO para la consulta con nombres
data class PartidaConNombresDto(
    val partidaId: Int,
    val fecha: String,
    val jugador1Id: Int,
    val jugador2Id: Int,
    val ganadorId: Int?,
    val esFinalizada: Boolean,
    val jugador1Nombre: String,
    val jugador2Nombre: String,
    val ganadorNombre: String?
)