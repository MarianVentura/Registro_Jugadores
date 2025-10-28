package edu.ucne.registrojugadoresmv.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrojugadoresmv.data.remote.MovimientoApi
import edu.ucne.registrojugadoresmv.data.remote.repository.MovimientoRepositoryImpl
import edu.ucne.registrojugadoresmv.domain.repository.MovimientoRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    private const val BASE_URL = "https://gestionhuacalesapi.azurewebsites.net/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideMovimientoApi(moshi: Moshi, okHttpClient: OkHttpClient): MovimientoApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(MovimientoApi::class.java)
}

@InstallIn(SingletonComponent::class)
@Module
abstract class MovimientoRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMovimientoRepository(
        movimientoRepositoryImpl: MovimientoRepositoryImpl
    ): MovimientoRepository
}