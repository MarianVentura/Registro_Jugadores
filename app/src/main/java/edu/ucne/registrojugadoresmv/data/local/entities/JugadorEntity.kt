package edu.ucne.registrojugadoresmv.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Jugadores")
data class Jugador(
    @PrimaryKey(autoGenerate = true)
    val jugadorId: Int = 0,
    val nombres: String,
    val partidas: Int
)