package com.example.myinventory.data.local.sources

import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.data.local.database.ProductInventoryDatabase
import com.example.myinventory.data.local.entity.ProductInventoryRemoteKeysEntity
import com.example.myinventory.domain.sources.LocalDataSource
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val productInventoryDatabase: ProductInventoryDatabase
) : LocalDataSource {

    private val productInventoryDao = productInventoryDatabase.getProductInventoryDao()
    private val productInventoryRemoteKeysDao =
        productInventoryDatabase.getProductInventoryRemoteKeysDao()

    override fun getAllProducts(): PagingSource<Int, ProductEntity> {
        return productInventoryDao.getAllProducts()
    }

    override suspend fun addProductInInventory(productList: List<ProductEntity>) {
        return productInventoryDao.addProductInInventory(productList)
    }

    override suspend fun deleteProductInventory() {
        productInventoryDao.deleteProductInventory()
    }

    override suspend fun getProductInventoryRemoteKeys(id: Int): ProductInventoryRemoteKeysEntity {
        return productInventoryRemoteKeysDao.getRemoteKeys(id)
    }

    override suspend fun addAllProductInventoryRemoteKeys(productRemoteKeysList: List<ProductInventoryRemoteKeysEntity>) {
        return productInventoryRemoteKeysDao.addAllRemoteKeys(productRemoteKeysList)
    }

    override suspend fun deleteProductInventoryRemoteKeys() {
        productInventoryRemoteKeysDao.deleteAllRemoteKeys()
    }

    override suspend fun storeProductsInInventoryAndRemoteKeysTransaction(
        productList: List<ProductEntity>,
        prevPage: Int?,
        nextPage: Int?,
        loadType: LoadType,
        lastTimeUpdated: Long
    ) {
        try {
            productInventoryDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    deleteProductInventory()
                    deleteProductInventoryRemoteKeys()
                }

                addProductInInventory(productList)
                val keys = productList.map { product ->
                    ProductInventoryRemoteKeysEntity(
                        id = product.id,
                        prevPage = prevPage,
                        nextPage = nextPage,
                        lastTimeUpdated = lastTimeUpdated
                    )
                }
                addAllProductInventoryRemoteKeys(keys)
            }
        } catch (_: Exception) {
        }
    }

    override suspend fun getFilteredProducts(
        productEntity: ProductEntity,
        searchQuery: String?
    ): List<ProductEntity> {
        return productInventoryDao.getFilteredProducts(
            productEntity.price,
            productEntity.productType,
            searchQuery,
            productEntity.tax
        )
    }
}