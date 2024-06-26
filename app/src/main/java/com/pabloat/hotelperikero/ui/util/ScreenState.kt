package com.pabloat.hotelperikero.ui.util

import com.pabloat.hotelperikero.data.local.entities.Espacio
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.data.local.entities.Resenia
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.data.local.entities.Servicio
import com.pabloat.hotelperikero.data.local.entities.ServicioEvento

sealed class ScreenState {
    object Loading : ScreenState()
    data class Error(val message: String) : ScreenState()
    data class Success(val habitaciones: List<Habitacion>) : ScreenState()
    data class SuccessEspacios(val espacios: List<Espacio>) : ScreenState()
    data class SuccessReservas(val reservas: List<Reserva>) : ScreenState()
    data class SuccessServiciosEventos(val serviciosEventos: List<ServicioEvento>) : ScreenState()
    data class SuccessReservasEventos(val reservasEventos: List<ReservaEventos>) : ScreenState()
    data class SuccessServicios(val servicios: List<Servicio>) : ScreenState()
    data class SuccessResenias(val resenias: List<Resenia>) : ScreenState()
}

