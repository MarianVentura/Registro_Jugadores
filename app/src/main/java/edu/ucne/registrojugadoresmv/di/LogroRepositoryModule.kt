package edu.ucne.registrojugadoresmv.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrojugadoresmv.data.repository.LogroRepositoryImpl
import edu.ucne.registrojugadoresmv.domain.repository.LogroRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LogroRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLogroRepository(
        logroRepositoryImpl: LogroRepositoryImpl
    ): LogroRepository
}