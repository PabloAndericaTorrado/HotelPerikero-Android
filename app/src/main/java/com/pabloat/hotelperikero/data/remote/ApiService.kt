package com.pabloat.hotelperikero.data.remote

import retrofit2.http.GET

/**
 * ApiService es una interfaz que se encarga de definir las peticiones que se pueden realizar a la API.
 */
interface ApiService {

    @GET("habitaciones")
    suspend fun getHabitaciones(): List<HabitacionDTO>
}


