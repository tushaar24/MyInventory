package com.example.myinventory.domain.repository

import androidx.paging.LoadType
import androidx.paging.PagingSource
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.data.local.entity.ProductInventoryRemoteKeysEntity
import com.example.myinventory.utils.NetworkResult

interface Repository {

    suspend fun getAllProducts(): PagingSource<Int, ProductEntity>

    suspend fun addProductInInventory(productList: List<ProductEntity>)

    suspend fun deleteProductInventory()

    suspend fun getProductInventoryRemoteKeys(id: Int): ProductInventoryRemoteKeysEntity

    suspend fun addAllProductInventoryRemoteKeys(productRemoteKeysList: List<ProductInventoryRemoteKeysEntity>)

    suspend fun deleteProductInventoryRemoteKeys()

    suspend fun storeProductsInInventoryAndRemoteKeysTransaction(
        productList: List<ProductEntity>,
        prevPage: Int?,
        nextPage: Int?,
        loadType: LoadType,
        lastTimeUpdated: Long
    )

    suspend fun getFilteredProducts(
        productEntity: ProductEntity,
        searchQuery: String?
    ): List<ProductEntity>

    suspend fun fetchAllProducts(): NetworkResult<List<ProductEntity>>
}