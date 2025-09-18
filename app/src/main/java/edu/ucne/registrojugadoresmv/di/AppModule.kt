package edu.ucne.registrojugadoresmv.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrojugadoresmv.data.local.dao.JugadorDao
import edu.ucne.registrojugadoresmv.data.local.dao.PartidaDao
import edu.ucne.registrojugadoresmv.data.local.database.AppDatabase
import edu.ucne.registrojugadoresmv.data.repository.JugadorRepositoryImpl
import edu.ucne.registrojugadoresmv.data.repository.PartidaRepositoryImpl
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import edu.ucne.registrojugadoresmv.domain.repository.PartidaRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "jugadores_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideJugadorDao(database: AppDatabase): JugadorDao {
        return database.jugadorDao()
    }

    @Provides
    fun providePartidaDao(database: AppDatabase): PartidaDao {
        return database.partidaDao()
    }

    @Provides
    @Singleton
    fun provideJugadorRepository(jugadorDao: JugadorDao): JugadorRepository {
        return JugadorRepositoryImpl(jugadorDao)
    }

    @Provides
    @Singleton
    fun providePartidaRepository(partidaDao: PartidaDao): PartidaRepository {
        return PartidaRepositoryImpl(partidaDao)
    }
}