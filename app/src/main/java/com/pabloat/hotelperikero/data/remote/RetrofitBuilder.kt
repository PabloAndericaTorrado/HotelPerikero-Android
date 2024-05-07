package com.pabloat.GameHubConnect.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitBuilder es un objeto que se encarga de crear una instancia de Retrofit para poder realizar peticiones a la API.
 * @property API_BASE_URL es la url base de la API.
 */
// Esta es la única línea que necesitamos cambiar generalmente, con la url de la API
private const val API_BASE_URL = "https://www.freetogame.com/api/"

object RetrofitBuilder {
    private fun getRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}