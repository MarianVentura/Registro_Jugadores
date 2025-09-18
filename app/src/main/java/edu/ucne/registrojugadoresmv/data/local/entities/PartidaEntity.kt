package edu.ucne.registrojugadoresmv.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "Partidas",
    foreignKeys = [
        ForeignKey(
            entity = Jugador::class,
            parentColumns = ["jugadorId"],
            childColumns = ["jugador1Id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Jugador::class,
            parentColumns = ["jugadorId"],
            childColumns = ["jugador2Id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Jugador::class,
            parentColumns = ["jugadorId"],
            childColumns = ["ganadorId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Partida(
    @PrimaryKey(autoGenerate = true)
    val partidaId: Int = 0,
    val fecha: String, // Usando String para simplificar la persistencia de fecha
    val jugador1Id: Int,
    val jugador2Id: Int,
    val ganadorId: Int? = null, // Nullable porque puede no haber ganador todavía
    val esFinalizada: Boolean = false
)