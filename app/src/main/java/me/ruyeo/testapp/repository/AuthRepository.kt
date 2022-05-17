package me.ruyeo.testapp.repository

import me.ruyeo.testapp.data.api.ApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(map: HashMap<String,Any>) = apiService.login(map)
}