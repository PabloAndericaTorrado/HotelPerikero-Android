package com.pabloat.hotelperikero.data;

import android.util.Log
import com.pabloat.hotelperikero.data.local.Habitacion;
import com.pabloat.hotelperikero.data.local.HotelDatasource;
import com.pabloat.hotelperikero.data.remote.RemoteHotelDataSource;

import kotlinx.coroutines.flow.Flow;

class HotelRepository(private val localds: HotelDatasource, private val remoteds: RemoteHotelDataSource) {
    suspend fun getRemoteHabitaciones(): List<Habitacion> {
        val habitacionesDTO = remoteds.getHabitaciones()
        if (habitacionesDTO.isEmpty()) {
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val habitacionesLocales = habitacionesDTO.map { it.toHabitacion() }
        localds.insertHabitacion(habitacionesLocales)
        return habitacionesLocales
    }



    suspend fun getLocalHabitacion(): Flow<List<Habitacion>> {
        return localds.getAllHabitaciones()
    }
}





