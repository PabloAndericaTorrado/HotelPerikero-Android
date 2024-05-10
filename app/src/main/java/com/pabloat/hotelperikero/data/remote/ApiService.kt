package com.pabloat.hotelperikero.data.remote

import retrofit2.http.GET

/**
 * ApiService es una interfaz que se encarga de definir las peticiones que se pueden realizar a la API.
 */
interface ApiService {

    @GET("habitaciones")
    suspend fun getHabitaciones(): HabitacionesDTO

    @GET("reservas")
    suspend fun getReservas(): ReservasDTO

    @GET("servicios")
    suspend fun getServicios():ServiciosDTO

    @GET("resenias")
    suspend fun getResenias(): ReseniasDTO
}


