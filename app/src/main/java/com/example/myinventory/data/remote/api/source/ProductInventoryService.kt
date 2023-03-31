package com.example.myinventory.data.remote.api.source

import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.utils.NetworkResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductInventoryService {
    @GET("get")
    suspend fun getAllProducts(): NetworkResult<List<ProductEntity>>

    @Multipart
    @POST("add")
    suspend fun addProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part("price") price: RequestBody,
        @Part imageFile: MultipartBody.Part?
    ): NetworkResult<ResponseBody>
}