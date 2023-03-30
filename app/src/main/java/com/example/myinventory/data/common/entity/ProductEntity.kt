package com.example.myinventory.data.common.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myinventory.utils.Constants.PRODUCT_INVENTORY_TABLE
import com.google.gson.annotations.SerializedName

@Entity(tableName = PRODUCT_INVENTORY_TABLE)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val image: String,
    val price: Double,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_type")
    val productType: String,
    val tax: Double
)
