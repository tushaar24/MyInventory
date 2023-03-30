package com.example.myinventory.di

import com.example.myinventory.data.local.database.ProductInventoryDatabase
import com.example.myinventory.data.local.sources.LocalDataSourceImpl
import com.example.myinventory.data.remote.api.source.ProductInventoryService
import com.example.myinventory.data.remote.sources.RemoteDataSourceImpl
import com.example.myinventory.domain.sources.LocalDataSource
import com.example.myinventory.domain.sources.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SourcesModule {

    @Provides
    @Singleton
    fun provideLocalDbSource(
        productInventoryDatabase: ProductInventoryDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(productInventoryDatabase)
    }

    @Provides
    @Singleton
    fun provideBeerApiSource(
        productInventoryService: ProductInventoryService
    ): RemoteDataSource {
        return RemoteDataSourceImpl(productInventoryService)
    }

}