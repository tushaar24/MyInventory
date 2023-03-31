package com.example.myinventory.domain.sources

import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.utils.NetworkResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Part

interface RemoteDataSource {

    suspend fun fetchAllProducts(): NetworkResult<List<ProductEntity>>

    suspend fun addProductOnServer(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part("price") price: RequestBody,
        @Part imageFile: MultipartBody.Part?
    ): NetworkResult<ResponseBody>
}