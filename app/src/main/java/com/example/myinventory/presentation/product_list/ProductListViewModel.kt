package com.example.myinventory.presentation.product_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _searchedProducts = MutableLiveData<List<ProductEntity>>()
    val searchProducts get() = _searchedProducts

    fun getAllCachedProducts() = repository.getAllCachedProducts()
        .cachedIn(viewModelScope)

    fun getSearchedProducts(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchedProducts.postValue(repository.getSearchedProducts(searchQuery))
        }
    }
}