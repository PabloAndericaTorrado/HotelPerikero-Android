package com.pabloat.hotelperikero.data.local

import android.content.Context
import android.util.Log
import com.pabloat.hotelperikero.data.local.dao.LocalEspacioDao
import com.pabloat.hotelperikero.data.local.dao.LocalHabitacionDao
import com.pabloat.hotelperikero.data.local.dao.LocalReseniaDao
import com.pabloat.hotelperikero.data.local.dao.LocalReservaDao
import com.pabloat.hotelperikero.data.local.dao.LocalReservaEventosDao
import com.pabloat.hotelperikero.data.local.dao.LocalServicioDao
import com.pabloat.hotelperikero.data.local.dao.LocalServicioEventoDao
import com.pabloat.hotelperikero.data.local.entities.Espacio
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.data.local.entities.Resenia
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.data.local.entities.Servicio
import com.pabloat.hotelperikero.data.local.entities.ServicioEvento
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class HotelDatasource (applicationContext: Context){
    private val db: AppDataBase = AppDataBase.getDatabase(applicationContext)
    private val habitacionDao: LocalHabitacionDao = db.habitacionDao()
    private val reservaDao: LocalReservaDao = db.reservaDao()
    private val servicioDao: LocalServicioDao = db.servicioDao()
    private val reseniaDao: LocalReseniaDao = db.reseniaDao()
    private val reservaEventosDao: LocalReservaEventosDao = db.reservaEventosDao()
    private val servicioEventoDao: LocalServicioEventoDao = db.servicioEventoDao()
    private val espacioDao: LocalEspacioDao = db.espacioDao()


    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getAllEspacios(): Flow<List<Espacio>> {
        return espacioDao.getAll().stateIn(GlobalScope)
    }

    suspend fun saveEspacios(espacios: List<Espacio>) {
        espacioDao.insertAll(espacios)
        checkDatabase()
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getAllServiciosEvento(): Flow<List<ServicioEvento>> {
        return servicioEventoDao.getAll().stateIn(GlobalScope)
    }

    suspend fun saveServicioEventos(servicioEvento: List<ServicioEvento>) {
        servicioEventoDao.insertAll(servicioEvento)
        checkDatabase()
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getAllReservasEventos(): Flow<List<ReservaEventos>> {
        return reservaEventosDao.getAll().stateIn(GlobalScope)
    }

    suspend fun saveReservasEventos(reservaEventos: List<ReservaEventos>) {
        reservaEventosDao.insertAll(reservaEventos)
        checkDatabase()
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getAllResenias(): Flow<List<Resenia>> {
        return reseniaDao.getAll().stateIn(GlobalScope)
    }

    suspend fun saveResenia(resenias: List<Resenia>) {
        reseniaDao.insertAll(resenias)
        checkDatabase()
    }

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

    suspend fun getHabitacionById(id: Int?): Habitacion? {
        return habitacionDao.getHabitacionById(id)
    }

    suspend fun getEspaciosById(id: Int?): Espacio? {
        return espacioDao.getEspacioById(id)
    }

    suspend fun insertReserva(reserva: Reserva) {
        reservaDao.insert(reserva)
    }

    suspend fun insertReservaevento(reserva: ReservaEventos) {
        reservaEventosDao.insert(reserva)
    }

    suspend fun getReservasByHabitacion(habitacionId: Int): List<Reserva> {
        return reservaDao.getReservasByHabitacion(habitacionId)
    }

    suspend fun getReservasByEspacio(espacioId: Int): List<ReservaEventos> {
        return reservaEventosDao.getReservasByEspacio(espacioId)
    }

    suspend fun getReservasByUserId(user_id: Int): List<Reserva> {
        return reservaDao.getReservasByUserId(user_id)
    }



    private suspend fun checkDatabase() {
        val habitaciones = habitacionDao.getAll().first()
        val reservas = reservaDao.getAll().first()
        val servicios = servicioDao.getAll().first()
        val resenias = reseniaDao.getAll().first()
        val reservaEventos = reservaEventosDao.getAll().first()
        val serviciosEventos = servicioEventoDao.getAll().first()
        val espacios = espacioDao.getAll().first()
        Log.d("DatabaseCheck", "Habitaciones en la base de datos: ${habitaciones.size}")
        Log.d("DatabaseCheck", "Reservas en la base de datos: ${reservas.size}")
        Log.d("DatabaseCheck", "servicios en la base de datos: ${servicios.size}")
        Log.d("DatabaseCheck", "Resenias en la base de datos: ${resenias.size}")
        Log.d("DatabaseCheck", "reservaEventos en la base de datos: ${reservaEventos.size}")
        Log.d("DatabaseCheck", "serviciosEventos en la base de datos: ${serviciosEventos.size}")
        Log.d("DatabaseCheck", "espacios en la base de datos: ${espacios.size}")
    }

    suspend fun getRandomHabitaciones(): List<Habitacion> {
        return habitacionDao.getRandomHabitaciones()
    }

}