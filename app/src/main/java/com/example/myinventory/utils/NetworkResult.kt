package com.example.myinventory.utils

import retrofit2.HttpException
import retrofit2.Response

sealed class NetworkResult<T> {
    sealed class Loading<T> : NetworkResult<T>() {
        class FromCache<T> : Loading<T>()
        class FromNetwork<T>(val isCacheFetchSuccessful: Boolean = true) : Loading<T>()
    }

    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Failure<T>(val code: Int, val message: String) : NetworkResult<T>()
    data class Exception<T>(val exception: Throwable) : NetworkResult<T>()

    companion object {
        fun <T> loading() = Loading.FromNetwork<T>()
        fun <T> loadingFromCache() = Loading.FromCache<T>()
        fun <T> loadingFromNetwork(isCacheFetchSuccessful: Boolean = true) =
            Loading.FromNetwork<T>(isCacheFetchSuccessful)

        fun <T> success(data: T) = Success(data)
        fun <T> failed(code: Int, message: String) = Failure<T>(code, message)
    }
}

fun <T> NetworkResult<T>.dataOrNull(): T? {
    return (this as? NetworkResult.Success)?.data
}

fun <T> NetworkResult<T>.exceptionOrNull(): Throwable? {
    return (this as? NetworkResult.Exception<T>)?.exception
}

suspend inline fun <V : Any?> NetworkResult<V>.onFailed(crossinline f: suspend (Throwable) -> Unit) =
    when (this) {
        is NetworkResult.Success -> NetworkResult.Success(data)
        is NetworkResult.Exception -> {
            f(exception)
            this
        }
        is NetworkResult.Failure -> this
        is NetworkResult.Loading.FromNetwork -> NetworkResult.loadingFromNetwork(this.isCacheFetchSuccessful)
        is NetworkResult.Loading.FromCache -> NetworkResult.loadingFromCache()
    }

suspend inline fun <V : Any?> NetworkResult<V>.onSuccess(crossinline f: suspend (V) -> Unit): NetworkResult<V> {
    return when (this) {
        is NetworkResult.Success -> {
            f(data)
            this
        }
        is NetworkResult.Exception -> this
        is NetworkResult.Failure -> this
        is NetworkResult.Loading.FromNetwork -> NetworkResult.loadingFromNetwork(this.isCacheFetchSuccessful)
        is NetworkResult.Loading.FromCache -> NetworkResult.loadingFromCache()
    }
}

fun <T : Any> handleApi(
    execute: () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else {
            NetworkResult.Failure(response.code(), response.message())
        }
    } catch (e: HttpException) {
        NetworkResult.Failure(e.code(), e.message())
    } catch (e: Exception) {
        NetworkResult.Exception(e)
    }
}