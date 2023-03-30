package com.example.myinventory.data.remote.api.utils

import com.example.myinventory.utils.NetworkResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NetworkResultCallAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<NetworkResult<Type>>> {
    override fun responseType(): Type {
        return resultType
    }

    override fun adapt(call: Call<Type>): Call<NetworkResult<Type>> {
        return NetworkResultCall(call)
    }
}