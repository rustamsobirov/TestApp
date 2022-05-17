package me.ruyeo.testapp.repository

import me.ruyeo.testapp.data.api.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getProducts() = apiService.getProducts()

    suspend fun getProduct(id: Int) = apiService.getProduct(id)
}