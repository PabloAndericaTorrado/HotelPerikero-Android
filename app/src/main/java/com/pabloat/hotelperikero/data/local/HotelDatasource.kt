package com.pabloat.hotelperikero.data.local

import android.content.Context
import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class HotelDatasource (applicationContext: Context){
    private val db: AppDataBase = AppDataBase.getDatabase(applicationContext)
    private val habitacionDao:LocalHabitacionDao = db.habitacionDao()


    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getAllHabitaciones(): Flow<List<Habitacion>> {
        return habitacionDao.getAll().stateIn(GlobalScope)
    }

    suspend fun saveHabitaciones(habitaciones: List<Habitacion>) {
        habitacionDao.insertAll(habitaciones)
        checkDatabase() // Llamar después de guardar
    }

    private suspend fun checkDatabase() {
        val habitaciones = habitacionDao.getAll().first()
        Log.d("DatabaseCheck", "Habitaciones en la base de datos: ${habitaciones.size}")
    }

}