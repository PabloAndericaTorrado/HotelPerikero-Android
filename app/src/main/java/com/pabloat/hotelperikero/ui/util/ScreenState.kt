package com.pabloat.hotelperikero.ui.util

import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.data.local.entities.Resenia
import com.pabloat.hotelperikero.data.local.entities.Servicio

sealed class ScreenState {
    object Loading : ScreenState()
    data class Error(val message: String) : ScreenState()
    data class Success(val habitacion: List<Habitacion>) : ScreenState()
    data class SuccessServicios(val servicios: List<Servicio>) : ScreenState()
    data class SuccessResenias(val resenias: List<Resenia>) : ScreenState()
}
