package edu.ucne.registrojugadoresmv.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrojugadoresmv.data.local.database.AppDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideJugadorDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Jugador.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideJugadorDao(jugadorDb: AppDatabase) = jugadorDb.jugadorDao()

    @Provides
    fun providePartidaDao(jugadorDb: AppDatabase) = jugadorDb.partidaDao()

    @Provides
    fun provideLogroDao(jugadorDb: AppDatabase) = jugadorDb.logroDao()  // ← NUEVO DAO AGREGADO
}