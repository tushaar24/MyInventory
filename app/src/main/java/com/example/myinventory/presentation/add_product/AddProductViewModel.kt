package com.example.myinventory.presentation.add_product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myinventory.domain.repository.Repository
import com.example.myinventory.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful get() = _isSuccessful

    fun addProductOnServer(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part("price") price: RequestBody,
        @Part imageFile: MultipartBody.Part?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.addProductOnServer(
                productName,
                productType,
                tax,
                price,
                imageFile
            )

            when (result) {
                is NetworkResult.Success -> {
                    _isSuccessful.postValue(true)
                }

                else -> {
                    _isSuccessful.postValue(false)
                }
            }
        }
    }
}