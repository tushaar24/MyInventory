package com.example.myinventory.data.remote.api.source

import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.utils.NetworkResult
import retrofit2.http.GET

interface ProductInventoryService {
    @GET("/add")
    suspend fun getAllProducts(): NetworkResult<List<ProductEntity>>
}