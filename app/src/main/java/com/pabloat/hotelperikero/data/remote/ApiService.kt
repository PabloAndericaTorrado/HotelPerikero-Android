package com.pabloat.GameHubConnect.data.remote

import retrofit2.http.GET

/**
 * ApiService es una interfaz que se encarga de definir las peticiones que se pueden realizar a la API.
 */
interface ApiService {

    @GET("games")
    suspend fun getVideoJuegos(): List<VideoJuegoDTO>
}


