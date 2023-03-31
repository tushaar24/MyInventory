package com.example.myinventory.data.common.repository

import androidx.paging.*
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.data.local.entity.ProductInventoryRemoteKeysEntity
import com.example.myinventory.data.remote.paging_sources.ProductInventoryRemoteMediator
import com.example.myinventory.domain.repository.Repository
import com.example.myinventory.domain.sources.LocalDataSource
import com.example.myinventory.domain.sources.RemoteDataSource
import com.example.myinventory.utils.NetworkResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override fun getAllProducts(): PagingSource<Int, ProductEntity> {
        return localDataSource.getAllProducts()
    }

    override suspend fun addProductInInventory(productList: List<ProductEntity>) {
        return localDataSource.addProductInInventory(productList)
    }

    override suspend fun deleteProductInventory() {
        return localDataSource.deleteProductInventory()
    }

    override suspend fun getProductInventoryRemoteKeys(id: Int): ProductInventoryRemoteKeysEntity {
        return localDataSource.getProductInventoryRemoteKeys(id)
    }

    override suspend fun addAllProductInventoryRemoteKeys(productRemoteKeysList: List<ProductInventoryRemoteKeysEntity>) {
        return addAllProductInventoryRemoteKeys(productRemoteKeysList)
    }

    override suspend fun deleteProductInventoryRemoteKeys() {
        localDataSource.deleteProductInventoryRemoteKeys()
    }

    override suspend fun storeProductsInInventoryAndRemoteKeysTransaction(
        productList: List<ProductEntity>,
        prevPage: Int?,
        nextPage: Int?,
        loadType: LoadType,
        lastTimeUpdated: Long
    ) {
        localDataSource.storeProductsInInventoryAndRemoteKeysTransaction(
            productList,
            prevPage,
            nextPage,
            loadType,
            lastTimeUpdated
        )
    }

    override suspend fun getFilteredProducts(
        productEntity: ProductEntity,
        searchQuery: String?
    ): List<ProductEntity> {
        return localDataSource.getFilteredProducts(
            productEntity,
            searchQuery
        )
    }

    override suspend fun fetchAllProducts(): NetworkResult<List<ProductEntity>> {
        return remoteDataSource.fetchAllProducts()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllCachedProducts() = Pager(
        config = PagingConfig(
            pageSize = 1
        ),
        remoteMediator = ProductInventoryRemoteMediator(this),
        pagingSourceFactory = { getAllProducts() }
    ).flow

    override suspend fun addProductOnServer(
        productName: RequestBody,
        productType: RequestBody,
        tax: RequestBody,
        price: RequestBody,
        imageFile: MultipartBody.Part?
    ): NetworkResult<ResponseBody> {
        return remoteDataSource.addProductOnServer(
            productName,
            productType,
            tax,
            price,
            imageFile
        )
    }

    override suspend fun getSearchedProducts(searchQuery: String): List<ProductEntity> {
        return localDataSource.getSearchedProducts(searchQuery)
    }
}