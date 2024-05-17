package com.pabloat.hotelperikero.data.remote

import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.remote.dtos.EspaciosDTO
import com.pabloat.hotelperikero.data.remote.dtos.HabitacionesDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReseniasDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservaEventosDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservasDTO
import com.pabloat.hotelperikero.data.remote.dtos.ServicioEventosDTO
import com.pabloat.hotelperikero.data.remote.dtos.ServiciosDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * ApiService es una interfaz que se encarga de definir las peticiones que se pueden realizar a la API.
 */
interface ApiService {

    @GET("habitaciones")
    suspend fun getHabitaciones(): HabitacionesDTO

    @GET("reservas")
    suspend fun getReservas(): ReservasDTO

    @GET("servicios")
    suspend fun getServicios(): ServiciosDTO

    @GET("resenias")
    suspend fun getResenias(): ReseniasDTO

    @GET("reservaEventos")
    suspend fun getReservaEventos(): ReservaEventosDTO

    @GET("servicioEventos")
    suspend fun getServiciosEvento(): ServicioEventosDTO

    @GET("espacios")
    suspend fun getEspacios(): EspaciosDTO

    @POST("reservas")
    suspend fun createReserva(@Body reserva: Reserva): Response<Reserva>
}


