package com.example.myinventory.domain.sources

import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.utils.NetworkResult

interface RemoteDataSource {
    suspend fun fetchAllProducts(): NetworkResult<List<ProductEntity>>
}