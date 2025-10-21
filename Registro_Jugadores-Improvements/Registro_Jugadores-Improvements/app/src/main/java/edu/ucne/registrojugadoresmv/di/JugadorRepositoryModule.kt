package edu.ucne.registrojugadoresmv.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrojugadoresmv.data.repository.JugadorRepositoryImpl
import edu.ucne.registrojugadoresmv.domain.repository.JugadorRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class JugadorRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindJugadorRepository(
        jugadorRepositoryImpl: JugadorRepositoryImpl
    ): JugadorRepository
}

