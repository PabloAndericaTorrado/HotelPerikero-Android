package com.pabloat.hotelperikero.data.local

import android.content.Context
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.stateIn

class HotelDatasource (applicationContext: Context){
    private val db: AppDataBase = AppDataBase.getDatabase(applicationContext)
    private val habitacionDao:LocalHabitacionDao = db.habitacionDao()


    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getAllHabitaciones(): Flow<List<Habitacion>> {
        return habitacionDao.getAll().stateIn(GlobalScope)
    }

    suspend fun insertHabitacion(habitaciones:List<Habitacion>){
        habitacionDao.insert(habitaciones)
    }

}