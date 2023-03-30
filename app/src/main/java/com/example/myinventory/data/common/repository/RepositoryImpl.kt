package com.example.myinventory.data.common.repository

import androidx.paging.LoadType
import androidx.paging.PagingSource
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.data.local.entity.ProductInventoryRemoteKeysEntity
import com.example.myinventory.domain.repository.Repository
import com.example.myinventory.domain.sources.LocalDataSource
import com.example.myinventory.domain.sources.RemoteDataSource
import com.example.myinventory.utils.NetworkResult
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun getAllProducts(): PagingSource<Int, ProductEntity> {
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

}