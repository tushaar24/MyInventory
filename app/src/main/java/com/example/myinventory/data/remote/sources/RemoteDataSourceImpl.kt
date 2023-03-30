package com.example.myinventory.data.remote.sources

import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.data.remote.api.source.ProductInventoryService
import com.example.myinventory.domain.sources.RemoteDataSource
import com.example.myinventory.utils.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val productInventoryService: ProductInventoryService
): RemoteDataSource{
    override suspend fun fetchAllProducts(): NetworkResult<List<ProductEntity>> {
        return productInventoryService.getAllProducts()
    }
}