package com.pabloat.hotelperikero.ui.util

import com.pabloat.hotelperikero.data.local.Habitacion

sealed class ScreenState {
    object Loading : ScreenState()
    data class Error(val message: String) : ScreenState()
    data class Success(val videojuego: List<Habitacion>) : ScreenState()
}
