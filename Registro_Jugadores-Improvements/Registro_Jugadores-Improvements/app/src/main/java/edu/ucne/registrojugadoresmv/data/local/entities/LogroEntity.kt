package edu.ucne.registrojugadoresmv.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Logros")
data class LogroEntity(
    @PrimaryKey(autoGenerate = true)
    val logroId: Int = 0,
    val nombre: String = "",
    val descripcion: String = ""
)