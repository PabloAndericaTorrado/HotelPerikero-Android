package com.pabloat.hotelperikero.data;

import android.util.Log
import com.pabloat.hotelperikero.data.local.HotelDatasource
import com.pabloat.hotelperikero.data.local.entities.Espacio
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.data.local.entities.Resenia
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.data.local.entities.ReservaParking
import com.pabloat.hotelperikero.data.local.entities.ReservaParkingAnonimo
import com.pabloat.hotelperikero.data.local.entities.ReservaServicio
import com.pabloat.hotelperikero.data.local.entities.Servicio
import com.pabloat.hotelperikero.data.local.entities.ServicioEvento
import com.pabloat.hotelperikero.data.remote.RemoteHotelDataSource
import com.pabloat.hotelperikero.data.remote.dtos.LastIdResponse
import kotlinx.coroutines.flow.Flow

class HotelRepository(
    private val localds: HotelDatasource,
    private val remoteds: RemoteHotelDataSource
) {


    // -------------------------- Reserva Parking Anonimos --------------------
    suspend fun getRemoteReservaParkingAnonimo(): List<ReservaParkingAnonimo> {
        val reservaParkingAnonDto = remoteds.getReservasParkingAnonimo()
        if (reservaParkingAnonDto.isEmpty()) {
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val reservasParkingLocales = reservaParkingAnonDto.map { it.toReservaParkingAnonimo() }
        Log.d("Repository", "Guardando reservas Servicios locales")
        localds.saveReservasParkingAnonimo(reservasParkingLocales)
        Log.d("Repository", "Servicios")
        return reservasParkingLocales
    }

    suspend fun saveReservaParkingAnon(reservaParking: List<ReservaParkingAnonimo>) {
        localds.saveReservasParkingAnonimo(reservaParking)
    }

    suspend fun getLocalReservasParkingAnonimo(): Flow<List<ReservaParkingAnonimo>> {
        return localds.getAllReservasParkingAnonimo()
    }


    // ------------------------------ Reservas Parking ------------------------
    suspend fun getRemoteReservaParking(): List<ReservaParking> {
        val reservaParkingDto = remoteds.getReservasParking()
        if (reservaParkingDto.isEmpty()) {
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val reservasParkingLocales = reservaParkingDto.map { it.toReservaParking() }
        Log.d("Repository", "Guardando reservas Servicios locales")
        localds.saveReservasParking(reservasParkingLocales)
        Log.d("Repository", "Servicios")
        return reservasParkingLocales
    }

    suspend fun saveReservaParking(reservaParking: List<ReservaParking>) {
        localds.saveReservasParking(reservaParking)
    }

    suspend fun getLocalReservasParking(): Flow<List<ReservaParking>> {
        return localds.getAllReservasParking()
    }

    // ------------------------------ Reservas Servicios ----------------------

    suspend fun getLastReservationId(): LastIdResponse {
        Log.d("Vakero", remoteds.getLastReservationId().toString())
        return remoteds.getLastReservationId()
    }
    suspend fun getRemoteReservaServicios(): List<ReservaServicio> {
        val reservaServicioDTO = remoteds.getReservaServicios()
        if (reservaServicioDTO.isEmpty()) {
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val reservasServiciosLocales = reservaServicioDTO.map { it.toReservaServicio() }
        Log.d("Repository", "Guardando reservas Servicios locales")
        localds.saveReservasServicios(reservasServiciosLocales)
        Log.d("Repository", "Servicios")
        return reservasServiciosLocales
    }

    suspend fun saveReservaServicios(reservaServicios: List<ReservaServicio>) {
        localds.saveReservasServicios(reservaServicios)
    }

    suspend fun getLocalReservasServicios(): Flow<List<ReservaServicio>> {
        return localds.getAllReservasServicios()
    }



    // ------------------------------ espacios --------------------------------

    suspend fun getRemoteEspacios(): List<Espacio> {
        val espacioDTO = remoteds.getEspacios()
        if (espacioDTO.isEmpty()) {
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val espaciosLocales = espacioDTO.map { it.toEspacio() }
        Log.d("Repository", "Guardando Servicios locales")
        localds.saveEspacios(espaciosLocales)
        Log.d("Repository", "Servicios")
        return espaciosLocales
    }

    suspend fun saveEspacios(espacios: List<Espacio>) {
        localds.saveEspacios(espacios)
    }

    suspend fun getLocalEspacios(): Flow<List<Espacio>> {
        return localds.getAllEspacios()
    }

    suspend fun getEspaciosById(id: Int?): Espacio? {
        return localds.getEspaciosById(id)
    }


// ------------------------------ ServiciosEventos --------------------------------

    suspend fun getRemoteServiciosEvento(): List<ServicioEvento> {
        val servicioEventoDTO = remoteds.getServiciosEventos()
        if (servicioEventoDTO.isEmpty()) {
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val serviciosEventosLocales = servicioEventoDTO.map { it.toServicioEvento() }
        Log.d("Repository", "Guardando Servicios locales")
        localds.saveServicioEventos(serviciosEventosLocales)
        Log.d("Repository", "Servicios")
        return serviciosEventosLocales
    }

    suspend fun saveServicioEvento(servicios: List<ServicioEvento>) {
        localds.saveServicioEventos(servicios)
    }

    suspend fun getLocalServiciosEventos(): Flow<List<ServicioEvento>> {
        return localds.getAllServiciosEvento()
    }


// ------------------------------ ReservasEventos --------------------------------

    suspend fun getRemoteReservasEventos(): List<ReservaEventos> {
        val reservasEventosDTO = remoteds.getReservaEventos()
        if (reservasEventosDTO.isEmpty()) {
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val reservasLocales = reservasEventosDTO.map { it.toReservaEventos() }
        Log.d("Repository", "Guardando Servicios locales")
        localds.saveReservasEventos(reservasLocales)
        Log.d("Repository", "Servicios")
        return reservasLocales
    }

    suspend fun saveReservaEvento(reservas: List<ReservaEventos>) {
        localds.saveReservasEventos(reservas)
    }

    suspend fun getLocalReservaEvento(): Flow<List<ReservaEventos>> {
        return localds.getAllReservasEventos()
    }

    suspend fun insertReservaEvento(reserva: ReservaEventos) {
        localds.insertReservaevento(reserva)
    }


    suspend fun getReservasByEspacio(espacioId: Int): List<ReservaEventos> {
        return localds.getReservasByEspacio(espacioId)
    }

    suspend fun getReservasEventosByUserId(user_id: Int): List<ReservaEventos> {
        return localds.getReservasEventosByUserId(user_id)
    }


// ------------------------------ Resenias --------------------------------

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

// ------------------------------ Servicios --------------------------------

    suspend fun getRemoteServicios(): List<Servicio> {
        val serviciosDTO = remoteds.getServicios()
        if (serviciosDTO.isEmpty()) {
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val serviciosLocales = serviciosDTO.map { it.toServicio() }
        Log.d("Repository", "Guardando Servicios locales")
        localds.saveServicios(serviciosLocales)
        Log.d("Repository", "Servicios")
        return serviciosLocales
    }

    suspend fun saveServicios(servicios: List<Servicio>) {
        localds.saveServicios(servicios)
    }

    suspend fun getLocalServicio(): Flow<List<Servicio>> {
        return localds.getAllServicios()
    }

    // ------------------------------ Reservas --------------------------------
    suspend fun getRemoteReservas(): List<Reserva> {
        val reservasDTO = remoteds.getReservas()
        if (reservasDTO.isEmpty()) {
            Log.d("Repository", "No rooms fetched from API")
            return emptyList()
        }
        val reservasLocales = reservasDTO.map { it.toReserva() }
        Log.d("Repository", "Guardando reservas locales")
        localds.saveReservas(reservasLocales)
        Log.d("Repository", "Reservas")
        return reservasLocales
    }

    suspend fun saveReservas(reservas: List<Reserva>) {
        localds.saveReservas(reservas)
    }

    suspend fun getLocalReserva(): Flow<List<Reserva>> {
        return localds.getAllReservas()
    }

    suspend fun getPastReservasByUserId(userId: Int): List<Reserva> {
        val reservas = remoteds.getPastReservasByUserId(userId)
        Log.d("HotelRepository", "Reservas obtenidas: $reservas")
        return reservas
    }


    suspend fun insertReserva(reserva: Reserva) {
        localds.insertReserva(reserva)
    }

    suspend fun getReservasByHabitacion(habitacionId: Int): List<Reserva> {
        return localds.getReservasByHabitacion(habitacionId)
    }

    suspend fun getReservasByUserId(user_id: Int): List<Reserva> {
        return localds.getReservasByUserId(user_id)
    }

    // ------------------------------ Habitaciones --------------------------------
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

    suspend fun getHabitacionById(id: Int?): Habitacion? {
        return localds.getHabitacionById(id)
    }


}





