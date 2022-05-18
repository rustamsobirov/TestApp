package me.ruyeo.testapp.ui.fragments.main.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.ruyeo.testapp.model.ErrorResponse
import me.ruyeo.testapp.model.Product
import me.ruyeo.testapp.repository.MainRepository
import me.ruyeo.testapp.utils.UiStateList
import me.ruyeo.testapp.utils.UiStateObject
import me.ruyeo.testapp.utils.extensions.userMessage
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _products = MutableStateFlow<UiStateList<Product>>(UiStateList.EMPTY)
    val products = _products

    fun getProducts() = viewModelScope.launch {
        _products.value = UiStateList.LOADING
        try {
            val response = repository.getProducts()
            if (response.code() >= 400){
                val error = Gson().fromJson(response.errorBody()!!.charStream(), ErrorResponse::class.java)
                _products.value = UiStateList.ERROR(error.errorMessage)
            }else{
                _products.value = UiStateList.SUCCESS(response.body()!!)
            }
        }catch (e : Exception){
            _products.value = UiStateList.ERROR(e.userMessage())
        }
    }

    private val _product = MutableStateFlow<UiStateObject<Product>>(UiStateObject.EMPTY)
    val product = _product

    fun getProduct(id: Int) = viewModelScope.launch {
        _product.value = UiStateObject.LOADING
        try {
            val response = repository.getProduct(id)
            if (response.code() >= 400){
                val error = Gson().fromJson(response.errorBody()!!.charStream(), ErrorResponse::class.java)
                _product.value = UiStateObject.ERROR(error.errorMessage)
            }else{
                _product.value = UiStateObject.SUCCESS(response.body()!!)
            }
        }catch (e: Exception){
            _product.value = UiStateObject.ERROR(e.userMessage())
        }
    }
}