package com.example.myinventory.data.remote.paging_sources

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.myinventory.MyInventoryApplication
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.data.local.entity.ProductInventoryRemoteKeysEntity
import com.example.myinventory.domain.repository.Repository
import com.example.myinventory.utils.NetworkResult
import kotlinx.coroutines.*
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ProductInventoryRemoteMediator @Inject constructor(
    private val repository: Repository
) : RemoteMediator<Int, ProductEntity>() {

    override suspend fun initialize(): InitializeAction {

        return withContext(MyInventoryApplication.AppScope.coroutineContext) {
            when (val response = repository.fetchAllProducts()) {
                is NetworkResult.Success -> {
                    val localData = repository.getProductInventory()
                    val initializeAction = if (localData == response.data) {
                        InitializeAction.SKIP_INITIAL_REFRESH
                    } else {
                        InitializeAction.LAUNCH_INITIAL_REFRESH
                    }
                    initializeAction
                }

                else -> {
                    val remoteKeys =
                        repository?.getProductInventoryRemoteKeys(0)?.lastTimeUpdated ?: 0
                    if (remoteKeys.toInt() != 0) {
                        InitializeAction.SKIP_INITIAL_REFRESH
                    } else {
                        InitializeAction.LAUNCH_INITIAL_REFRESH
                    }
                }
            }
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeysForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            when (val response = repository.fetchAllProducts()) {
                is NetworkResult.Success -> {
                    Log.d("oxoxtushar", "network call: ${response.data.size}")
                    val endOfPaginationReached = true
                    val prevPage = if (currentPage == 1) null else currentPage.minus(1)
                    val nextPage = if (endOfPaginationReached) null else currentPage.plus(1)

                    repository.storeProductsInInventoryAndRemoteKeysTransaction(
                        response.data,
                        prevPage,
                        nextPage,
                        loadType,
                        System.currentTimeMillis()
                    )

                    MediatorResult.Success(endOfPaginationReached)
                }

                is NetworkResult.Exception -> {
                    MediatorResult.Error(response.exception)
                }

                else -> {
                    MediatorResult.Error(Exception("Something went wrong"))
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ProductEntity>): ProductInventoryRemoteKeysEntity? {
        return state.anchorPosition?.let { remoteKey ->
            state.closestItemToPosition(remoteKey)?.let {
                repository.getProductInventoryRemoteKeys(it.id)
            }
        }
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, ProductEntity>): ProductInventoryRemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { product ->
                repository.getProductInventoryRemoteKeys(product.id)
            }
    }
}