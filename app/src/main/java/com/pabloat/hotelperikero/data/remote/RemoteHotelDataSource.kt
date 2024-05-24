package com.pabloat.hotelperikero.data.remote

import android.util.Log
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.remote.dtos.EspacioDTO
import com.pabloat.hotelperikero.data.remote.dtos.HabitacionDTO
import com.pabloat.hotelperikero.data.remote.dtos.LastIdResponse
import com.pabloat.hotelperikero.data.remote.dtos.ReseniaDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservaDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservaEventoDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservaParkingAnonimoDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservaParkingDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservaServicioDTO
import com.pabloat.hotelperikero.data.remote.dtos.ReservasResponse
import com.pabloat.hotelperikero.data.remote.dtos.ServicioDTO
import com.pabloat.hotelperikero.data.remote.dtos.ServicioEventoDTO
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RemoteHotelDataSource(private val apiService: ApiService) {
    suspend fun getHabitaciones(): List<HabitacionDTO> {
        // Suponiendo que HabitacionesDTO envuelve la respuesta de la API que contiene la lista de habitaciones
        val response = apiService.getHabitaciones()
        return response.data  // Asegúrate de que 'data' es la lista de HabitacionDTO
    }
    suspend fun getReservas(): List<ReservaDTO>{
        val response = apiService.getReservas()
        return response.data
    }

    suspend fun getServicios(): List<ServicioDTO>{
        val response = apiService.getServicios()
        return response.data
    }

    suspend fun getResenias(): List<ReseniaDTO> {
        val response = apiService.getResenias()
        return response.data
    }

    suspend fun getReservaEventos(): List<ReservaEventoDTO> {
        val response = apiService.getReservaEventos()
        return response.data
    }

    suspend fun getServiciosEventos(): List<ServicioEventoDTO> {
        val response = apiService.getServiciosEvento()
        return response.data
    }

    suspend fun getEspacios(): List<EspacioDTO> {
        val response = apiService.getEspacios()
        return response.data
    }

    suspend fun getReservaServicios(): List<ReservaServicioDTO> {
        val response = apiService.getReservasServicios()
        return response.data
    }

    suspend fun getReservasParking(): List<ReservaParkingDTO> {
        val response = apiService.getReservasParking()
        return response.data
    }

    suspend fun getReservasParkingAnonimo(): List<ReservaParkingAnonimoDTO> {
        val response = apiService.getReservasParkingAnon()
        return response.data
    }

    suspend fun getLastReservationId(): LastIdResponse {
        return apiService.getLastReservaId()
    }

    suspend fun getPastReservasByUserId(userId: Int): List<Reserva> =
        suspendCancellableCoroutine { continuation ->
            val call = apiService.getPastReservasByUserId(mapOf("userId" to userId))
            call.enqueue(object : Callback<ReservasResponse> {
                override fun onResponse(
                    call: Call<ReservasResponse>,
                    response: Response<ReservasResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("RemoteHotelDataSource", "API call successful: ${response.body()}")
                        continuation.resume(response.body()?.data ?: emptyList())
                    } else {
                        Log.e(
                            "RemoteHotelDataSource",
                            "API call failed: ${response.errorBody()?.string()}"
                        )
                        continuation.resume(emptyList())
                    }
                }

                override fun onFailure(call: Call<ReservasResponse>, t: Throwable) {
                    Log.e("RemoteHotelDataSource", "API call failed with exception", t)
                    if (continuation.isActive) {
                        continuation.resumeWithException(t)
                    }
                }
            })

            continuation.invokeOnCancellation {
                call.cancel()
            }
        }


}
