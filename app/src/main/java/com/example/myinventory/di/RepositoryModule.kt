package com.example.myinventory.di

import com.example.myinventory.data.common.repository.RepositoryImpl
import com.example.myinventory.domain.repository.Repository
import com.example.myinventory.domain.sources.LocalDataSource
import com.example.myinventory.domain.sources.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        local: LocalDataSource,
        remote: RemoteDataSource
    ): Repository {
        return RepositoryImpl(local, remote)
    }
}