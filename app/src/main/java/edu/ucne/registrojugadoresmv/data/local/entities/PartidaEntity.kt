package edu.ucne.registrojugadoresmv.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Partidas",
    foreignKeys = [
        ForeignKey(
            entity = JugadorEntity::class,
            parentColumns = ["jugadorId"],
            childColumns = ["jugador1Id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = JugadorEntity::class,
            parentColumns = ["jugadorId"],
            childColumns = ["jugador2Id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = JugadorEntity::class,
            parentColumns = ["jugadorId"],
            childColumns = ["ganadorId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class PartidaEntity(
    @PrimaryKey(autoGenerate = true)
    val partidaId: Int = 0,
    val fecha: String = "",
    val jugador1Id: Int = 0,
    val jugador2Id: Int = 0,
    val ganadorId: Int? = null,
    val esFinalizada: Boolean = false
)