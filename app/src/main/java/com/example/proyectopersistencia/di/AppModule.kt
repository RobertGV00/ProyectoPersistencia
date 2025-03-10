package com.example.proyectopersistencia.di

import com.example.proyectopersistencia.repository.ClimaRepository
import com.example.proyectopersistencia.viewmodel.AppBBDD
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideCiudadDao(db: AppBBDD) = db.ciudadDao()

    @Provides
    @Singleton
    fun provideClimaRepository(): ClimaRepository {
        return ClimaRepository()
    }
}