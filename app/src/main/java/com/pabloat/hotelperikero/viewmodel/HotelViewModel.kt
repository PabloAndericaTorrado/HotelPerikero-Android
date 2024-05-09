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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HotelViewModel(private val repository: HotelRepository) : ViewModel() {

    private val _habitaciones: MutableStateFlow<List<Habitacion>> = MutableStateFlow(listOf())
    var habitacones = _habitaciones.asStateFlow()

    private val _uiState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)

    private val handler = CoroutineExceptionHandler { _, exception ->
        _uiState.value =
            ScreenState.Error("Ha ocurrido un error, revise su conexión a internet o inténtelo de nuevo más tarde")
    }

    fun getRemoteHabitacion() {
        viewModelScope.launch(handler) {
            delay(5000)
            Log.d("VM", "Lanzamos petición a la API")
            val remoteHabitaciones: List<Habitacion> =
                repository.getRemoteHabitacones()
            // Recogemos el resultado
            _habitaciones.value = remoteHabitaciones
            // Actualizamos el estado
            Log.d("VM", "Info obtenida: $remoteHabitaciones")
        }
        Log.d("VM", "Petición lanzada. Los datos irán llegando...")
    }

}