package com.example.myinventory.domain.repository

import androidx.paging.LoadType
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.data.local.entity.ProductInventoryRemoteKeysEntity
import com.example.myinventory.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Part

interface Repository {

    fun getAllProducts(): PagingSource<Int, ProductEntity>

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

    fun getAllCachedProducts(): Flow<PagingData<ProductEntity>>

    suspend fun getSearchedProducts(searchQuery: String): List<ProductEntity>

    suspend fun addProductOnServer(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part("price") price: RequestBody,
        @Part imageFile: MultipartBody.Part?
    ): NetworkResult<ResponseBody>

}