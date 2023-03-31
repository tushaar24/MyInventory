package com.example.myinventory.data.remote.sources

import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.data.remote.api.source.ProductInventoryService
import com.example.myinventory.domain.sources.RemoteDataSource
import com.example.myinventory.utils.NetworkResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val productInventoryService: ProductInventoryService
): RemoteDataSource {
    override suspend fun fetchAllProducts(): NetworkResult<List<ProductEntity>> {
        return productInventoryService.getAllProducts()
    }

    override suspend fun addProductOnServer(
        productName: RequestBody,
        productType: RequestBody,
        tax: RequestBody,
        price: RequestBody,
        imageFile: MultipartBody.Part?
    ): NetworkResult<ResponseBody> {
        return productInventoryService.addProduct(
            productName,
            productType,
            tax,
            price,
            imageFile
        )
    }
}