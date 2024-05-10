package com.pabloat.hotelperikero.ui.util

import com.pabloat.hotelperikero.data.local.Habitacion
import com.pabloat.hotelperikero.data.local.Servicio

sealed class ScreenState {
    object Loading : ScreenState()
    data class Error(val message: String) : ScreenState()
    data class Success(val habitacion: List<Habitacion>) : ScreenState()
    data class SuccessServicios(val servicios: List<Servicio>) : ScreenState()
}
