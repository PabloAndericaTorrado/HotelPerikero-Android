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
    private val reservaDao: LocalReservaDao = db.reservaDao()
    private val servicioDao:LocalServicioDao = db.servicioDao()

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getAllServicios(): Flow<List<Servicio>> {
        return servicioDao.getAll().stateIn(GlobalScope)
    }

    suspend fun saveServicios(servicios: List<Servicio>) {
        servicioDao.insertAll(servicios)
        checkDatabase()
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getAllReservas(): Flow<List<Reserva>> {
        return reservaDao.getAll().stateIn(GlobalScope)
    }

    suspend fun saveReservas(reservas: List<Reserva>) {
        reservaDao.insertAll(reservas)
        checkDatabase()
    }
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
        val reservas = reservaDao.getAll().first()
        val servicios = servicioDao.getAll().first()
        Log.d("DatabaseCheck", "Habitaciones en la base de datos: ${habitaciones.size}")
        Log.d("DatabaseCheck", "Reservas en la base de datos: ${reservas.size}")
        Log.d("DatabaseCheck", "Reservas en la base de datos: ${servicios.size}")
    }

    suspend fun getRandomHabitaciones(): List<Habitacion> {
        return habitacionDao.getRandomHabitaciones()
    }

}