package me.ruyeo.testapp.data.api

import me.ruyeo.testapp.model.Product
import me.ruyeo.testapp.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

   @POST("authenticate")
   suspend fun login(@Body map: HashMap<String, Any>): Response<User>

   @GET("products")
   suspend fun getProducts(): Response<List<Product>>

   @GET("products/{id}")
   suspend fun getProduct(@Path("id") id : Int): Response<Product>
}