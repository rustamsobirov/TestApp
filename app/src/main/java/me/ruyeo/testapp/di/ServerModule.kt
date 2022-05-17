package me.ruyeo.testapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ruyeo.testapp.SharedPref
import me.ruyeo.testapp.data.api.ApiService
import me.ruyeo.testapp.utils.Constants.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServerModule {

    @Provides
    @Singleton
    fun getRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun getApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun getClient(sharedPref: SharedPref): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.header("Content-Type", "application/json")
            builder.header("Accept", "application/json")
              if (sharedPref.token != ""){
                  builder.header("Authorization", "Token ${sharedPref.token}")
              }
            chain.proceed(builder.build())
        })
        .build()

}