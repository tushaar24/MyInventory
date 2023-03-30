package com.example.myinventory.di

import android.content.Context
import androidx.room.Room
import com.example.myinventory.data.local.database.ProductInventoryDatabase
import com.example.myinventory.utils.Constants.PRODUCT_INVENTORY_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDbModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ProductInventoryDatabase::class.java,
        PRODUCT_INVENTORY_DATABASE
    ).build()
}