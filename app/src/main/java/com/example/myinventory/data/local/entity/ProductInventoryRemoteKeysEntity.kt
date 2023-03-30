package com.example.myinventory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myinventory.utils.Constants.PRODUCT_INVENTORY_REMOTE_KEYS_TABLE

@Entity(tableName = PRODUCT_INVENTORY_REMOTE_KEYS_TABLE)
data class ProductInventoryRemoteKeysEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val prevPage: Int?,
    val nextPage: Int?,
    val lastTimeUpdated: Long?
)
