package com.pabloat.GameHubConnect.ui.util

import com.pabloat.GameHubConnect.data.local.Videojuego
import kotlinx.coroutines.flow.Flow

sealed class ScreenState {
    object Loading : ScreenState()
    data class Error(val message: String) : ScreenState()
    data class Success(val videojuego: Flow<List<Videojuego>>) : ScreenState()
}
