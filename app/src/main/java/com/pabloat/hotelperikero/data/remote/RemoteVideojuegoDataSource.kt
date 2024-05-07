package com.pabloat.GameHubConnect.data.remote

/**
 * RemoteVideojuegoDataSource es una clase que se encarga de gestionar la obtención de datos de la API.
 * @param apiService es la interfaz que se encarga de realizar las peticiones a la API.
 */
class RemoteVideojuegoDataSource(private val apiService: ApiService) {
    suspend fun getVideojuegos() =
        apiService.getVideoJuegos()
}