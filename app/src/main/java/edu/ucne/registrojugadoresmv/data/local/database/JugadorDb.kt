package edu.ucne.registrojugadoresmv.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import edu.ucne.registrojugadoresmv.data.local.dao.JugadorDao
import edu.ucne.registrojugadoresmv.data.local.dao.PartidaDao
import edu.ucne.registrojugadoresmv.data.local.entities.Jugador
import edu.ucne.registrojugadoresmv.data.local.entities.Partida

@Database(
    entities = [Jugador::class, Partida::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao
    abstract fun partidaDao(): PartidaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jugadores_database"
                )
                    .fallbackToDestructiveMigration() // Para desarrollo, elimina datos en cambio de esquema
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}