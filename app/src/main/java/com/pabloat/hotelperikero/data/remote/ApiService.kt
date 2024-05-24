package com.pabloat.hotelperikero.data.remote

import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.data.local.entities.ReservaServicio
import com.pabloat.hotelperikero.data.remote.dtos.EspaciosDTO
import com.pabloat.hotelperikero.data.remote.dtos.HabitacionesDTO
import com.pabloat.hotelperikero.data.remote.dtos.LastIdResponse
import com.pabloat.hotelperikero.data.remote.dtos.ReseniasDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservaEventosDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservaServiciosDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservasDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservasParkingAnonimosDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservasParkingDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservasResponse
import com.pabloat.hotelperikero.data.remote.dtos.ServicioEventosDTO
import com.pabloat.hotelperikero.data.remote.dtos.ServiciosDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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

    @POST("reservas/create")
    fun createReserva(@Body reserva: Reserva): Call<Reserva>

    @POST("reservaEventos/create")
    fun createReservaEventos(@Body reservaEvento: ReservaEventos): Call<ReservaEventos>

    @POST("reservaServicios/create")
    fun createReservaServicios(@Body reservaServicio: ReservaServicio): Call<ReservaServicio>

    @GET("reservaServicio")
    suspend fun getReservasServicios(): ReservaServiciosDTO

    @GET("reservaParking")
    suspend fun getReservasParking(): ReservasParkingDTO

    @GET("reservaParkingAnonimo")
    suspend fun getReservasParkingAnon(): ReservasParkingAnonimosDTO

    @GET("reservas/lastId")
    suspend fun getLastReservaId(): LastIdResponse

    @POST("reservas/past")
    fun getPastReservasByUserId(@Body userId: Map<String, Int>): Call<ReservasResponse>
}


