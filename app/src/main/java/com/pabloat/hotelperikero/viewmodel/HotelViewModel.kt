package com.pabloat.hotelperikero.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pabloat.hotelperikero.data.HotelRepository
import com.pabloat.hotelperikero.data.local.entities.Espacio
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.data.local.entities.Resenia
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.data.local.entities.Servicio
import com.pabloat.hotelperikero.data.local.entities.ServicioEvento
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

    private val _reservas = MutableStateFlow<List<Reserva>>(emptyList())
    val reservas: StateFlow<List<Reserva>> = _reservas.asStateFlow()

    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios.asStateFlow()

    private val _resenias = MutableStateFlow<List<Resenia>>(emptyList())
    val resenias: StateFlow<List<Resenia>> = _resenias.asStateFlow()

    private val _reservasEventos = MutableStateFlow<List<ReservaEventos>>(emptyList())
    val reservasEventos: StateFlow<List<ReservaEventos>> = _reservasEventos.asStateFlow()

    private val _serviciosEventos = MutableStateFlow<List<ServicioEvento>>(emptyList())
    val serviciosEventos: StateFlow<List<ServicioEvento>> = _serviciosEventos.asStateFlow()

    private val _espacios = MutableStateFlow<List<Espacio>>(emptyList())
    val espacios: StateFlow<List<Espacio>> = _espacios.asStateFlow()

    private val _uiState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)

    private val handler = CoroutineExceptionHandler { _, exception ->
        _uiState.value =
            ScreenState.Error("Ha ocurrido un error, revise su conexión a internet o inténtelo de nuevo más tarde")
    }
    fun fetchEspacios() = viewModelScope.launch {
        _uiState.value = ScreenState.Loading
        try {
            val espacios = repository.getRemoteEspacios()
            _espacios.value = espacios
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
        }
    }

    fun fetchServiciosEventos() = viewModelScope.launch {
        _uiState.value = ScreenState.Loading
        try {
            val servicioEvento = repository.getRemoteServiciosEvento()
            _serviciosEventos.value = servicioEvento
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
        }
    }

    fun fetchReservasEventos() = viewModelScope.launch {
        _uiState.value = ScreenState.Loading
        try {
            val reservaEventos = repository.getRemoteReservasEventos()
            _reservasEventos.value = reservaEventos
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
        }
    }


    fun fetchResenias() = viewModelScope.launch {
        _uiState.value = ScreenState.Loading
        try {
            val resenias = repository.getRemoteResenias()
            _resenias.value = resenias
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
        }
    }


    fun fetchReservas() = viewModelScope.launch {
        _uiState.value = ScreenState.Loading
        try {
            val reservas = repository.getRemoteReservas()
            _reservas.value = reservas
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
        }
    }
    fun fetchServicios() = viewModelScope.launch {
        _uiState.value = ScreenState.Loading
        try {
            val servicios = repository.getRemoteServicios()
            _servicios.value = servicios
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
        }
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

    private val _randomHabitaciones = MutableStateFlow<List<Habitacion>>(emptyList())
    val randomHabitaciones: StateFlow<List<Habitacion>> = _randomHabitaciones.asStateFlow()

    // Cargar habitaciones aleatorias y actualizar el StateFlow
    fun fetchRandomRooms() = viewModelScope.launch {
        _uiState.value = ScreenState.Loading
        try {
            val rooms = repository.getRandomLocalHabitaciones()
            if (rooms.isNotEmpty()) {
                _randomHabitaciones.value = rooms
                _uiState.value = ScreenState.Success(rooms)
            } else {
                _uiState.value = ScreenState.Error("No se encontraron habitaciones")
            }
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
        }
    }

    // Método para cargar servicios de la base de datos local
    fun loadLocalServices() = viewModelScope.launch {
        _uiState.value = ScreenState.Loading
        try {
            // Collecting from the flow and updating the state
            repository.getLocalServicio().collect { localServices ->
                _servicios.value = localServices
                if (localServices.isNotEmpty()) {
                    _uiState.value = ScreenState.SuccessServicios(localServices)
                } else {
                    _uiState.value = ScreenState.Error("No se encontraron servicios")
                }
            }
        } catch (e: Exception) {
            _uiState.value = ScreenState.Error("Error al cargar servicios locales: ${e.message}")
        }
    }

    fun fetchRandomResenias(){
        viewModelScope.launch {
            _uiState.value = ScreenState.Loading
            try {
                val resenias = repository.getRemoteResenias()
                if (resenias.isNotEmpty()) {
                    _resenias.value = resenias
                    _uiState.value = ScreenState.SuccessResenias(resenias)
                } else {
                    _uiState.value = ScreenState.Error("No se encontraron reseñas")
                }
            } catch (e: Exception) {
                _uiState.value = ScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private var HabitacionId: Int? = null


    suspend fun getHabitacionByIdFlow(id: Int?): Habitacion? {
        return repository.getHabitacionById(id)
    }

    var habitacionssss: Habitacion? = null

    fun getHabitacionById(id: Int?): Habitacion? {
        viewModelScope.launch {
            HabitacionId = id
            val habitacion = getHabitacionByIdFlow(id)
            habitacionssss = habitacion
            Log.d("VM", "Habitación obtenida: $habitacion")
        }
        return habitacionssss
    }

    private val _selectedHabitacionId = MutableStateFlow<Int?>(null)
    val selectedHabitacionId = _selectedHabitacionId.asStateFlow()

    fun selectHabitacionId(id: Int?) {
        _selectedHabitacionId.value = id
    }










}
