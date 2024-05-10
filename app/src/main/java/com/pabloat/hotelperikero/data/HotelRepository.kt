package com.pabloat.hotelperikero.data;

import android.util.Log
import com.pabloat.hotelperikero.data.local.Habitacion;
import com.pabloat.hotelperikero.data.local.HotelDatasource;
import com.pabloat.hotelperikero.data.local.Resenia
import com.pabloat.hotelperikero.data.local.Reserva
import com.pabloat.hotelperikero.data.local.Servicio
import com.pabloat.hotelperikero.data.remote.RemoteHotelDataSource;

import kotlinx.coroutines.flow.Flow;

class HotelRepository(private val localds: HotelDatasource, private val remoteds: RemoteHotelDataSource) {
    suspend fun getRemoteResenias(): List<Resenia> {
        val reseniasDTO = remoteds.getResenias()
        if (reseniasDTO.isEmpty()) {
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val reseniasLocales = reseniasDTO.map { it.toResenia() }
        Log.d("Repository", "Guardando Servicios locales")
        localds.saveResenia(reseniasLocales)
        Log.d("Repository", "Servicios")
        return reseniasLocales
    }

    suspend fun saveResenia(resenias: List<Resenia>) {
        localds.saveResenia(resenias)
    }

    suspend fun getLocalResenia(): Flow<List<Resenia>> {
        return localds.getAllResenias()
    }

    suspend fun getRemoteServicios():List<Servicio>{
        val serviciosDTO = remoteds.getServicios()
        if(serviciosDTO.isEmpty()){
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val serviciosLocales = serviciosDTO.map { it.toServicio() }
        Log.d("Repository", "Guardando Servicios locales")
        localds.saveServicios(serviciosLocales)
        Log.d("Repository", "Servicios")
        return serviciosLocales
    }

    suspend fun saveServicios(servicios: List<Servicio>){
        localds.saveServicios(servicios)
    }

    suspend fun getLocalServicio(): Flow<List<Servicio>> {
        return localds.getAllServicios()
    }
    suspend fun getRemoteReservas():List<Reserva>{
        val reservasDTO = remoteds.getReservas()
        if (reservasDTO.isEmpty()){
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val reservasLocales = reservasDTO.map { it.toReserva() }
        Log.d("Repository", "Guardando reservas locales")
        localds.saveReservas(reservasLocales)
        Log.d("Repository", "Reservas")
        return reservasLocales
    }

    suspend fun saveReservas(reservas: List<Reserva>){
        localds.saveReservas(reservas)
    }
    suspend fun getLocalReserva(): Flow<List<Reserva>> {
        return localds.getAllReservas()
    }


    suspend fun getRemoteHabitaciones(): List<Habitacion> {
        val habitacionesDTO = remoteds.getHabitaciones()
        if (habitacionesDTO.isEmpty()) {
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val habitacionesLocales = habitacionesDTO.map { it.toHabitacion() }
        Log.d("Repository", "Guardando habitaciones locales")
        localds.saveHabitaciones(habitacionesLocales)
        Log.d("Repository", "Habitaciones guardadas")
        return habitacionesLocales
    }


    suspend fun saveHabitaciones(habitaciones: List<Habitacion>) {
        localds.saveHabitaciones(habitaciones)
    }

    suspend fun getLocalHabitacion(): Flow<List<Habitacion>> {
        return localds.getAllHabitaciones()
    }

    suspend fun getRandomLocalHabitaciones(): List<Habitacion> {
        return localds.getRandomHabitaciones()
    }
}





