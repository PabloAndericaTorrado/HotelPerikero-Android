package com.pabloat.hotelperikero.data;

import com.pabloat.hotelperikero.data.local.Habitacion;
import com.pabloat.hotelperikero.data.local.HotelDatasource;
import com.pabloat.hotelperikero.data.remote.RemoteHotelDataSource;

import kotlinx.coroutines.flow.Flow;

class HotelRepository(
    private val localds: HotelDatasource,
    private val remoteds: RemoteHotelDataSource
) {
    suspend fun getRemoteHabitacones(

    ): ArrayList<Habitacion> {
        val habitaciones = remoteds.getHabitaciones()
        val habitacionesLocales = ArrayList<Habitacion>()

        for (habitacion in habitaciones) {
            habitacionesLocales.add(habitacion.toHabitacion())
        }
        localds.insertHabitacion(habitacionesLocales)
        return habitacionesLocales
        //val listaVideojuegos = remoteds.getVideojuegos().toLocalList()
        //return listaVideojuegos
    }

    suspend fun getLocalHabitacion():Flow<List<Habitacion>>
    {
        return localds.getAllHabitaciones()
    }



}
