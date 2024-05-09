package com.pabloat.hotelperikero.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pabloat.hotelperikero.data.HotelRepository
import com.pabloat.hotelperikero.data.local.Habitacion
import com.pabloat.hotelperikero.ui.util.ScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HotelViewModel(private val repository: HotelRepository) : ViewModel() {

    private val _habitaciones = MutableStateFlow<List<Habitacion>>(emptyList())
    val habitaciones: StateFlow<List<Habitacion>> = _habitaciones.asStateFlow()

    private val _uiState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)

    private val handler = CoroutineExceptionHandler { _, exception ->
        _uiState.value =
            ScreenState.Error("Ha ocurrido un error, revise su conexión a internet o inténtelo de nuevo más tarde")
    }

    fun getRemoteHabitacion() {
        viewModelScope.launch(handler) {
            delay(5000)  // Asegúrate de que este delay es necesario
            Log.d("VM", "Lanzamos petición a la API")
            val remoteHabitaciones: List<Habitacion> = repository.getRemoteHabitaciones()  // Corregido aquí
            _habitaciones.value = remoteHabitaciones
            Log.d("VM", "Info obtenida: $remoteHabitaciones")
        }
        Log.d("VM", "Petición lanzada. Los datos irán llegando...")
    }

    fun fetchRooms() = viewModelScope.launch {
        _uiState.value = ScreenState.Loading
        try {
            val rooms = repository.getRemoteHabitaciones() // Esto debería también guardar en la base de datos
            _habitaciones.value = rooms
            _uiState.value = ScreenState.Success(rooms)
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
        }
    }

    fun loadLocalRooms() = viewModelScope.launch {
        val localRooms = repository.getLocalHabitacion().collect { rooms ->
            _habitaciones.value = rooms
            _uiState.value = ScreenState.Success(rooms)
        }
    }
}
