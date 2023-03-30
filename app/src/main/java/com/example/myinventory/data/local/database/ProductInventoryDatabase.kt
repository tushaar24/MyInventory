package com.example.myinventory.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.data.local.dao.ProductInventoryDao
import com.example.myinventory.data.local.dao.ProductInventoryRemoteKeysDao
import com.example.myinventory.data.local.entity.ProductInventoryRemoteKeysEntity

@Database(
    entities = [ProductEntity::class, ProductInventoryRemoteKeysEntity::class],
    version = 1
)
abstract class ProductInventoryDatabase : RoomDatabase() {
    abstract fun getProductInventoryDao(): ProductInventoryDao
    abstract fun getProductInventoryRemoteKeysDao(): ProductInventoryRemoteKeysDao
}