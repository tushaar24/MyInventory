package com.example.myinventory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myinventory.data.local.entity.ProductInventoryRemoteKeysEntity
import com.example.myinventory.utils.Constants.PRODUCT_INVENTORY_REMOTE_KEYS_TABLE

@Dao
interface ProductInventoryRemoteKeysDao {
    @Query("SELECT * FROM $PRODUCT_INVENTORY_REMOTE_KEYS_TABLE WHERE id =:id")
    suspend fun getRemoteKeys(id: Int): ProductInventoryRemoteKeysEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeysList: List<ProductInventoryRemoteKeysEntity>)

    @Query("DELETE FROM $PRODUCT_INVENTORY_REMOTE_KEYS_TABLE")
    suspend fun deleteAllRemoteKeys()
}