package edu.ucne.registrojugadoresmv.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Jugadores")
data class JugadorEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombres: String,
    val email: String,
    val partidas: Int = 0,
    val isPendingCreate: Boolean = false
)