package com.example.myinventory.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.utils.Constants.PRODUCT_INVENTORY_TABLE

@Dao
interface ProductInventoryDao {
    @Query("SELECT * FROM $PRODUCT_INVENTORY_TABLE")
    fun getAllProducts(): PagingSource<Int, ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProductInInventory(productList: List<ProductEntity>)

    @Query("DELETE FROM $PRODUCT_INVENTORY_TABLE")
    suspend fun deleteProductInventory()

    @Query("SELECT * FROM $PRODUCT_INVENTORY_TABLE WHERE price <= :price OR :price IS NULL AND productType = :productType OR :productType IS NULL AND productName LIKE :searchQuery OR :searchQuery IS NULL AND tax <= :tax OR :tax IS NULL")
    fun getFilteredProducts(
        price: Double?,
        productType: String?,
        searchQuery: String?,
        tax: Double?
    ): List<ProductEntity>

    @Query("SELECT * FROM $PRODUCT_INVENTORY_TABLE WHERE productName LIKE '%' || :searchQuery || '%'")
    fun getSearchedProducts(
        searchQuery: String
    ): List<ProductEntity>

    @Query("SELECT * FROM $PRODUCT_INVENTORY_TABLE")
    fun getProductInventory(): List<ProductEntity>
}